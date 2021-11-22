package com.example.famdif_final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TiendaSeleccionadaFragment extends BaseFragment {
    private TextView tNombre;
    private TextView nombre;
    private TextView tDireccion;
    private TextView direccion;
    private TextView tTipo;
    private TextView tipo;
    private TextView tSubtipo;
    private TextView subtipo;
    private TextView tAccesibilidad;
    private TextView accesibilidad;
    private TextView puntuacionObtenida;
    private ImageView imagen;
    private ImageView imagen1;
    private StorageReference mStorageReference;
    private Button btnEditarTienda;
    private Button valorarTienda;
    private RatingBar ratingBar;
    private int contVotos;
    private double puntTotal;
    private PhotoViewAttacher foto;
    private PhotoViewAttacher foto2;

    private Tienda tiendaSeleccionada;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        tiendaSeleccionada=Controlador.getInstance().getSelectedShop();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tienda_seleccionada, container, false);
        getMainActivity().getSupportActionBar().setTitle("Búsqueda - "+tiendaSeleccionada.getNombre());
        obtenerImagen();
        tNombre=view.findViewById(R.id.idNombreTiendaSeleccionada);
        nombre=view.findViewById(R.id.nombreTiendaSeleccionada);
        nombre.setText(tiendaSeleccionada.getNombre());
        tDireccion=view.findViewById(R.id.iddireccionTiendaSeleccionada);
        direccion=view.findViewById(R.id.direccionTiendaSeleccionada);
        direccion.setText(tiendaSeleccionada.getDireccion());
        tTipo=view.findViewById(R.id.idTipoEstablecimientoSeleccionado);
        tipo=view.findViewById(R.id.tipoEstablecimientoSeleccionado);
        tipo.setText(tiendaSeleccionada.getTipo());
        tSubtipo=view.findViewById(R.id.idSubtipoEstablecimientoSeleccionado);
        subtipo=view.findViewById(R.id.subtipoEstablecimientoSeleccionado);
        subtipo.setText(tiendaSeleccionada.getSubtipo());
        tAccesibilidad=view.findViewById(R.id.idAccesibilidadTiendaSeleccionada);
        accesibilidad=view.findViewById(R.id.accesibilidadTiendaSeleccionada);
        accesibilidad.setText(tiendaSeleccionada.getClasificacion());
        imagen=view.findViewById(R.id.image_view_upload);
        imagen1=view.findViewById(R.id.image_view_upload1);
        foto= new PhotoViewAttacher(imagen);
        foto2= new PhotoViewAttacher(imagen1);


        //puntuaciones

        valorarTienda=view.findViewById(R.id.btnValorar);
        valorarTienda.setVisibility(View.INVISIBLE);
        if(Controlador.getInstance().getUsuario()!=null){
            valorarTienda.setVisibility(View.VISIBLE);
        }
        ratingBar=view.findViewById(R.id.ratingBar);
        puntuacionObtenida=view.findViewById(R.id.idPuntuacionObtenida);

        btnEditarTienda=view.findViewById(R.id.btnEditarTienda);
        if(Controlador.getInstance().getAdmin()==1){
            btnEditarTienda.setVisibility(View.VISIBLE);
            btnEditarTienda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMainActivity().getSupportActionBar().setTitle("EDITAR TIENDA - "+tiendaSeleccionada.getNombre());
                    getMainActivity().setFragment(FragmentName.EDIT_SHOP);
                }
            });
        }

        valorarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarVotacion();
            }
        });

        obtenerImagen();
        obtenerPuntuacion();
        return view;

    }

    private void realizarVotacion() {
        Map<String, Object> v = new HashMap<>();
        Votacion nuevaVotacion = new Votacion(Controlador.getInstance().getUsuario(),Controlador.getInstance().getSelectedShop().getId(),String.valueOf(ratingBar.getRating()));
        MainActivity.db.collection("votaciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Votacion votacion = document.toObject(Votacion.class);
                            if(document.getId().matches(nuevaVotacion.getEmail()+nuevaVotacion.getId())){
                                if(votacion.getPuntuacion().matches(nuevaVotacion.getPuntuacion())){
                                    Toast.makeText(getContext(), "Ya tiene una votación con" +
                                            " la misma puntuación", Toast.LENGTH_SHORT).show();
                                }else{
                                    MainActivity.db.collection("votaciones")
                                            .document(document.getId())
                                            .update("puntuacion",nuevaVotacion.getPuntuacion());
                                    Toast.makeText(getContext(), "Se ha actualizado la nueva puntuación", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Map<String, Object> v = new HashMap<>();
                                v.put("id",nuevaVotacion.getId());
                                v.put("email",nuevaVotacion.getEmail());
                                v.put("puntuacion",nuevaVotacion.getPuntuacion());

                                MainActivity.db.collection("votaciones").document(nuevaVotacion.getEmail()+nuevaVotacion.getId())
                                        .set(v);
                            }

                        }
                        obtenerPuntuacion();
                    }

                });

    }

    private void obtenerPuntuacion() {
        Log.i("Entramos a obtener puntuacion","PUNTUACION");
        puntTotal=0;
        contVotos=0;
        MainActivity.db.collection("votaciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Votacion votacion = document.toObject(Votacion.class);
                            if(votacion.getId().toString().matches(tiendaSeleccionada.getId())) {
                                Log.i("Se cumple condicion",votacion.getId().toString() +" - "+ tiendaSeleccionada.getId());
                                contVotos++;
                                puntTotal += Double.valueOf(votacion.getPuntuacion());
                                Log.i("Votacion encontrada",String.valueOf(puntTotal));
                            }

                        }
                        ratingBar.setRating((float) puntTotal/contVotos);
                        puntuacionObtenida.setText("Puntuación: "+ratingBar.getRating()+" - "+contVotos+" votos");
                    }

                });



    }


    private void obtenerImagen() {
        mStorageReference = FirebaseStorage.getInstance().getReference("/"+tiendaSeleccionada.getId()+"_A.jpg");

        try {
            File localfile = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imagen.setImageBitmap(bitmap);
                    foto.update();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStorageReference = FirebaseStorage.getInstance().getReference("/"+tiendaSeleccionada.getId()+"_B.jpg");

        try {
            File localfile1 = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                    imagen1.setImageBitmap(bitmap);
                    foto2.update();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
