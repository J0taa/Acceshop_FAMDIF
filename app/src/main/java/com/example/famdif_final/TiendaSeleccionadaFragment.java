package com.example.famdif_final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
    private ImageView imagen;
    private ImageView imagen1;
    private StorageReference mStorageReference;

    private Tienda tiendaSeleccionada;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        tiendaSeleccionada=Controlador.getInstance().getSelectedShop();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tienda_seleccionada, container, false);
        getMainActivity().getSupportActionBar().setTitle("Búsqueda - "+tiendaSeleccionada.getNombre());
        Log.i("TiendaSeleccionada:",tiendaSeleccionada.getNombre());
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
        accesibilidad.setText(tiendaSeleccionada.getAccesibilidad());
        imagen=view.findViewById(R.id.image_view_upload);
        imagen1=view.findViewById(R.id.image_view_upload1);
        obtenerImagen();
        return view;

    }

    private void obtenerImagen() {
        mStorageReference = FirebaseStorage.getInstance().getReference("Uploads/"+tiendaSeleccionada.getId()+"_1.jpg");

        try {
            File localfile = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imagen.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStorageReference = FirebaseStorage.getInstance().getReference("Uploads/"+tiendaSeleccionada.getId()+"_2.jpg");

        try {
            File localfile1 = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                    imagen1.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
