package com.example.famdif_final.fragment;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Tienda;
import com.example.famdif_final.Votacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    private TextView acceso;
    private TextView puertaAcceso;
    private TextView fechaCreacion;
    private TextView fechaModificacion;
    private TextView idPuntTienda;
    private ImageView imagen;
    private ImageView imagen1;
    private StorageReference mStorageReference;
    private Button btnEditarTienda;
    private Button valorarTienda;
    private Button btnEliminarTienda;
    private RatingBar ratingBar;
    private RatingBar ratingBarMedium;
    private int contVotos;
    private double puntTotal;
    private double mipuntuacion;
    private PhotoViewAttacher foto;
    private PhotoViewAttacher foto2;
    private TextView puntuacionMediaObtenida;

    private Tienda tiendaSeleccionada;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        tiendaSeleccionada= Controlador.getInstance().getSelectedShop();
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
        accesibilidad.setText(tiendaSeleccionada.getClasificacion()+" - "+parsearAccesibilidadBBDD(tiendaSeleccionada.getClasificacion()));
        acceso=view.findViewById(R.id.idAccesoObtenido);
        acceso.setText(tiendaSeleccionada.getAcceso());
        puertaAcceso=view.findViewById(R.id.idPuertaObtenida);
        puertaAcceso.setText(tiendaSeleccionada.getPuertaAcceso());
        fechaCreacion=view.findViewById(R.id.idFechaCreacionObtenida);
        fechaCreacion.setText(tiendaSeleccionada.getFechaVisita());
        idPuntTienda=view.findViewById(R.id.idPuntuacionTienda);

        imagen=view.findViewById(R.id.image_view_upload);
        imagen1=view.findViewById(R.id.image_view_upload1);

        foto= new PhotoViewAttacher(imagen);
        foto2= new PhotoViewAttacher(imagen1);

        ratingBarMedium=view.findViewById(R.id.ratingBarMed);
        puntuacionMediaObtenida=view.findViewById(R.id.idPuntuacionMediaObtenida);

        ratingBar=view.findViewById(R.id.ratingBar);
        puntuacionObtenida=view.findViewById(R.id.idPuntuacionObtenida);

        btnEliminarTienda=view.findViewById(R.id.btnEliminarTienda);
        btnEditarTienda=view.findViewById(R.id.btnEditarTienda);
        valorarTienda=view.findViewById(R.id.btnValorar);
        valorarTienda.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);
        puntuacionObtenida.setVisibility(View.INVISIBLE);
        idPuntTienda.setVisibility(View.INVISIBLE);
        btnEliminarTienda.setVisibility(View.INVISIBLE);

        if(Controlador.getInstance().getUsuario()!=null){
            if(Controlador.getInstance().getAdmin()==1){
                btnEliminarTienda.setVisibility(View.VISIBLE);
            }
            valorarTienda.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            puntuacionObtenida.setVisibility(View.VISIBLE);
            idPuntTienda.setVisibility(View.VISIBLE);
        }

        if(Controlador.getInstance().getUsuario()!=null){
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

        btnEliminarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Está seguro de que desea borrar la siguiente tienda?:"+ "\n"+"\n" + tiendaSeleccionada.getId() +"-"+tiendaSeleccionada.getNombre())
                        .setTitle("CONFIRMAR BORRADO");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarTienda();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        obtenerImagen();
        obtenerPuntuacion();
        Log.i("TIENDA ",tiendaSeleccionada.getAcceso()+" "+tiendaSeleccionada.getPuertaAcceso());
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
        mipuntuacion=0;
        MainActivity.db.collection("votaciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Votacion votacion = document.toObject(Votacion.class);
                            if(votacion.getId().toString().matches(tiendaSeleccionada.getId())) {
                                if(Controlador.getInstance().getUsuario()!=null && votacion.getEmail().matches(Controlador.getInstance().getUsuario())){
                                    mipuntuacion=Double.valueOf(votacion.getPuntuacion());
                                }
                                contVotos++;
                                puntTotal += Double.valueOf(votacion.getPuntuacion());
                            }
                        }
                        ratingBarMedium.setRating((float) puntTotal/contVotos);
                        puntuacionMediaObtenida.setText("Puntuación: "+ratingBarMedium.getRating()+" - "+contVotos+" votos");
                        ratingBar.setRating((float) mipuntuacion);
                        puntuacionObtenida.setText("Puntuación: "+ratingBar.getRating() );
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

    private String parsearAccesibilidadBBDD(String accesMin){
        String cadena="";
        switch (accesMin){
            case "A":
                cadena="ACCESIBLE";
                break;
            case "B":
                cadena="ACCESIBLE CON DIFICULTAD";
                break;
            case "C":
                cadena="PRACTICABLE CON AYUDA";
                break;
            case "D":
                cadena="MALA ACCESIBILIDAD";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return cadena;
    }

    private void eliminarTienda() {
        MainActivity.db.collection("votaciones")
                .whereEqualTo("id",tiendaSeleccionada.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                MainActivity.db.collection("votaciones")
                                        .document(documentSnapshot.getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }

                        }
                    }
                });

        MainActivity.db.collection("comerciosElCarmenTest")
                .whereEqualTo("id",tiendaSeleccionada.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                MainActivity.db.collection("comerciosElCarmenTest")
                                        .document(document.getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Error borrando la tienda", e.getMessage());
                                        Toast.makeText(getContext(),"No se ha podido borrar la tienda",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }
                });

        Toast.makeText(getContext(),"Se ha borrado la tienda correctamente",Toast.LENGTH_LONG).show();
        getMainActivity().setFragment(FragmentName.HOME);
        getMainActivity().clearBackStack();

    }


}
