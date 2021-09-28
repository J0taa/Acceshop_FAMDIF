package com.example.famdif_final;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CrearTiendaFragment extends BaseFragment {
    private static final String TAG = "log1";
    private EditText id;
    private EditText nombreTienda;
    private Spinner tipoTienda;
    private Spinner subtipoTienda;
    private EditText direccion;
    private Spinner accesibilidad;
    private EditText latitud;
    private EditText longitud;
    private Button creaTienda;
    private Button getLocation;
    private Date creacion;
    private Date modificacion;
    private TipoTienda tTienda;
    private SubtipoTienda stTienda;

    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private List<String> listaAcces = Arrays.asList("ACCESIBLE", "ACCESIBLE CON DIFICULTAD", "PRACTICABLE CON AYUDA", "CUALQUIERA");

    private String tipoSeleccionado;
    private String subtipoSeleccionado;
    private String accesibSeleccionada;

    private Button seleccionarImagenTienda;
    private EditText nombreImagen;
    private Uri mImageUri;
    private ImageView mImageView;

    private GoogleMap mMap;
    private LocationManager ubicacion;


    public CrearTiendaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setMainActivity((MainActivity) getActivity());
        View view = inflater.inflate(R.layout.fragment_crear_tienda, container, false);

        tipoTienda = view.findViewById(R.id.crearTiendaDespleglableTipo);
        subtipoTienda = view.findViewById(R.id.crearTiendaDespleglableSubtipo);
        accesibilidad = view.findViewById(R.id.crearTiendaDespleglableAccesibilidad);

        id = view.findViewById(R.id.creaTiendaId);
        nombreTienda = view.findViewById(R.id.creaTiendaNombre);
        direccion = view.findViewById(R.id.creaTiendaDireccion);
        latitud = view.findViewById(R.id.creaTiendaLatitud);
        longitud = view.findViewById(R.id.creaTiendaLongitud);
        creaTienda = view.findViewById(R.id.btnCrearTienda);
        getLocation = view.findViewById(R.id.btnGetLocation);

        seleccionarImagenTienda = view.findViewById(R.id.btnCargarImagen);
        nombreImagen = view.findViewById(R.id.editTextImagen);
        mImageView = view.findViewById(R.id.imagenCargada);


        tTienda = new TipoTienda();
        adapt = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, tTienda.getTiposTienda());
        tipoTienda.setAdapter(adapt);

        stTienda = new SubtipoTienda("Alimentación");
        adapt1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, stTienda.getSubTiposTienda());
        subtipoTienda.setAdapter(adapt1);

        adapt2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, listaAcces);
        accesibilidad.setAdapter(adapt2);

        tipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoSeleccionado = (String) tipoTienda.getAdapter().getItem(position);
                SubtipoTienda subtTienda = new SubtipoTienda(tipoSeleccionado);
                ArrayAdapter adapt4 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, subtTienda.getSubTiposTienda());
                subtipoTienda.setAdapter(adapt4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        seleccionarImagenTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }

        });


        subtipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subtipoSeleccionado = (String) subtipoTienda.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accesibilidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accesibSeleccionada = (String) accesibilidad.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUbicacion();
            }
        });


        creaTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creacion = new Date();
                modificacion = new Date();
                Tienda t = new Tienda(id.getText().toString(), nombreTienda.getText().toString(), tipoSeleccionado,
                        subtipoSeleccionado, direccion.getText().toString(), accesibSeleccionada, latitud.getText().toString()
                        , longitud.getText().toString(), creacion, modificacion);
                crearTienda(t);
                uploadFile();
            }


        });


        getMainActivity().getSupportActionBar().setTitle("CREAR TIENDA");
        getMainActivity().changeMenu(MenuType.ADMIN_LOGGED);
        getMainActivity().setOptionMenu(R.id.item_add_shop);
        return view;
    }


    @SuppressLint("MissingPermission")
    private void getUbicacion() {
        if (!getMainActivity().getGpsManager().checkPermissions()) {
            getMainActivity().getGpsManager().requestPermissions();

        } else {

            getMainActivity().getGpsManager().getFusedLocationProviderClient().getLastLocation().addOnCompleteListener(getMainActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        latitud.setText(Double.toString(task.getResult().getLatitude()));
                        longitud.setText(Double.toString(task.getResult().getLongitude()));
                    }
                }
            });
        }
    }

    private void crearTienda(Tienda t) {
        Map<String, Object> tienda = new HashMap<>();
        tienda.put("id", t.getId());
        tienda.put("nombre", t.getNombre());
        tienda.put("tipo", t.getTipo());
        tienda.put("subtipo", t.getSubtipo());
        tienda.put("direccion", t.getDireccion());
        tienda.put("accesibilidad", t.getAccesibilidad());
        tienda.put("latitud", t.getLatitud());
        tienda.put("longitud", t.getLongitud());
        tienda.put("creacion", t.getFechaRegistro());
        tienda.put("modificacion", t.getFechaModificacion());

        MainActivity.databaseReference.child("shops").child(String.valueOf(t.hashCode())).setValue(tienda);

    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(intent,  MainActivity.PICK_IMAGE_REQUEST) ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  MainActivity.PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData()!=null ){
            mImageUri =data.getData();
            Picasso.with(this.getContext()).load(mImageUri).into(mImageView);
        }
    }



    private String getFileExtension(Uri  uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadFile(){
        if(mImageUri != null){
            //StorageReference storageReference = MainActivity.mStorageRef.child(System.currentTimeMillis() + "." +getFileExtension(mImageUri));
            StorageReference storageReference = MainActivity.mStorageRef.child(id.getText().toString()+"_1" + "." +getFileExtension(mImageUri));
            storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                    Upload upload = new Upload(nombreImagen.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                }
            });

        }else{
            Toast.makeText(getContext(), "No se ha seleccionado imagen", Toast.LENGTH_LONG).show();
        }
    }
}