package com.example.famdif_final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EditShopFragment extends BaseFragment {
    private TipoTienda tipoTiendaLista;
    private SubtipoTienda subtipoTiendaLista;
    private Spinner tipoTienda;
    private Spinner subtipoTienda;
    private Spinner despAccesibilidad;
    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private Tienda tienda;

    private EditText identificador;
    private EditText nombreTienda;
    private EditText direccion;

    private ImageView imagen1;
    private ImageView imagen2;

    private List<String> accesibilidad= Arrays.asList("ACCESIBLE", "ACCESIBLE CON DIFICULTAD","PRACTICABLE CON AYUDA","CUALQUIERA");


    public EditShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());
        tienda=Controlador.getInstance().getSelectedShop();
        final View view = inflater.inflate(R.layout.fragment_edit_shop, container, false);
        tipoTienda=view.findViewById(R.id.desplegableTipoEstablecimiento);
        subtipoTienda=view.findViewById(R.id.desplegablesubtipoEstablecimiento);
        despAccesibilidad=view.findViewById(R.id.desplegableAccesibilidad);


        tipoTiendaLista = new TipoTienda();

        tipoTienda.setSelection(getIndex(tipoTienda, tienda.getTipo()));
        subtipoTiendaLista = new SubtipoTienda(tienda.getTipo());
        despAccesibilidad.setSelection(getIndex(despAccesibilidad, tienda.getClasificacion()));

        adapt = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,tipoTiendaLista.getTiposTienda());
        tipoTienda.setAdapter(adapt);

        adapt1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtipoTiendaLista.getSubTiposTienda());
        subtipoTienda.setAdapter(adapt1);

        adapt2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,accesibilidad);
        despAccesibilidad.setAdapter(adapt2);

        identificador=view.findViewById(R.id.idIdentificadorTienda);
        identificador.setText(tienda.getId());
        nombreTienda=view.findViewById(R.id.textoNombreTienda);
        nombreTienda.setText(tienda.getNombre());
        direccion=view.findViewById(R.id.textoDireccion);
        direccion.setText(tienda.getDireccion());

        imagen1=view.findViewById(R.id.image_view_upload);
        imagen2=view.findViewById(R.id.image_view_upload1);

        obtenerImagen();

        return view;
    }

    private int getIndex(Spinner tipoTienda, String tipo) {
        for (int i=0;i<tipoTienda.getCount();i++){
            if (tipoTienda.getItemAtPosition(i).toString().equalsIgnoreCase(tipo)){
                return i;
            }
        }
        return 0;
    }

    private void obtenerImagen() {
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads/" + tienda.getId() + "_1.jpg");

        try {
            File localfile = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    imagen1.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStorageReference = FirebaseStorage.getInstance().getReference("Uploads/"+tienda.getId()+"_2.jpg");

        try {
            File localfile1 = File.createTempFile("tempfile", "jpg");
            mStorageReference.getFile(localfile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                    imagen2.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
