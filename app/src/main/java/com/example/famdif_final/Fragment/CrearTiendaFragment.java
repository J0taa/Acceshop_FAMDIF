package com.example.famdif_final.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
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

import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.MenuType;
import com.example.famdif_final.R;
import com.example.famdif_final.SubtipoTienda;
import com.example.famdif_final.Tienda;
import com.example.famdif_final.TipoTienda;
import com.example.famdif_final.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearTiendaFragment extends BaseFragment {
    private static final String TAG = "log1";
    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;
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
    private String creacion;
    private TipoTienda tTienda;
    private SubtipoTienda stTienda;
    private EditText acceso;
    private EditText puertaAcceso;

    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private List<String> listaAcces = Arrays.asList("ACCESIBLE", "ACCESIBLE CON DIFICULTAD", "PRACTICABLE CON AYUDA", "CUALQUIERA");

    private String tipoSeleccionado;
    private String subtipoSeleccionado;
    private String accesibSeleccionada;

    private Button seleccionarImagenTienda;
    private Button seleccionarImagenTienda1;
    //private EditText nombreImagen;
    //private EditText nombreImagen1;
    private Uri mImageUri;
    private Uri mImageUri1;
    private ImageView mImageView;
    private ImageView mImageView1;


    private Tienda tiendaCreada;




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
        acceso = view.findViewById(R.id.creaTiendaAcceso);
        puertaAcceso = view.findViewById(R.id.creaTiendaPuertaAcceso);

        id = view.findViewById(R.id.creaTiendaId);
        nombreTienda = view.findViewById(R.id.creaTiendaNombre);
        direccion = view.findViewById(R.id.creaTiendaDireccion);
        latitud = view.findViewById(R.id.creaTiendaLatitud);
        longitud = view.findViewById(R.id.creaTiendaLongitud);
        creaTienda = view.findViewById(R.id.btnCrearTienda);
        getLocation = view.findViewById(R.id.btnGetLocation);

        seleccionarImagenTienda = view.findViewById(R.id.btnCargarImagen);
        //nombreImagen = view.findViewById(R.id.editTextImagen);
        mImageView = view.findViewById(R.id.imagenCargada);

        seleccionarImagenTienda1 = view.findViewById(R.id.btnCargarImagen1);
        //nombreImagen1 = view.findViewById(R.id.editTextImagen1);
        mImageView1 = view.findViewById(R.id.imagenCargada1);


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

        seleccionarImagenTienda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser2();
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
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                creacion = df.format(new Date()).toString();
                tiendaCreada = new Tienda(id.getText().toString(), nombreTienda.getText().toString(), tipoSeleccionado,
                        subtipoSeleccionado, direccion.getText().toString(), accesibSeleccionada, latitud.getText().toString()
                        , longitud.getText().toString(), creacion, acceso.getText().toString(), puertaAcceso.getText().toString());
                crearTienda(tiendaCreada);
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
        tienda.put("clasificacion", parsearAccesibilidadTextoNumero(t.getClasificacion()));
        tienda.put("latitud", t.getLatitud());
        tienda.put("longitud", t.getLongitud());
        tienda.put("creacion", t.getCreacion());
        tienda.put("acceso",t.getAcceso());
        tienda.put("puertaAcceso",t.getPuertaAcceso());

        MainActivity.db.collection("comerciosElCarmenTest").document(id.getText().toString())
        .set(tienda);

    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(intent, PICK_IMAGE_1) ;
    }

    private void openFileChooser2(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  PICK_IMAGE_1 && resultCode == -1 && data != null && data.getData()!=null ){
                mImageUri = data.getData();
                Picasso.with(this.getContext()).load(mImageUri).into(mImageView);

        }else if(requestCode ==  PICK_IMAGE_2 && resultCode == -1 && data != null && data.getData()!=null ) {
                mImageUri1 = data.getData();
                Picasso.with(this.getContext()).load(mImageUri1).into(mImageView1);
            }

    }

    private String getFileExtension(Uri  uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadFile(){
        if(mImageUri != null){
            Log.i("2-","LA IMAGEN NO ES NULA");
            //StorageReference storageReference = MainActivity.mStorageRef.child(System.currentTimeMillis() + "." +getFileExtension(mImageUri));
            StorageReference storageReference = MainActivity.mStorageRef.child(id.getText().toString()+"_A" +
                    "." +getFileExtension(mImageUri));
            Log.i("3-",storageReference.toString());
            storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Upload upload = new Upload(id.getText().toString()+"_A", uri.toString());
                            Log.i("4-","IMAGEN SUBIDA");
                            Toast.makeText(getContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("FOTOS:",e.getMessage());
                        }
                    });

                }
            });
        }
        if(mImageUri1!=null){
            StorageReference storageReference = MainActivity.mStorageRef.child(id.getText().toString()+"_B" +
                    "." +getFileExtension(mImageUri1));
            storageReference.putFile(mImageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Upload upload = new Upload(id.getText().toString()+"_B", uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("FOTOS:",e.getMessage());
                        }
                    });

                }
            });
        }
        if(mImageUri1==null && mImageUri==null)
            Toast.makeText(getContext(), "No se han seleccionado imagenes", Toast.LENGTH_LONG).show();

        Toast.makeText(getContext(), "Tienda creada correctamente", Toast.LENGTH_LONG).show();
        getMainActivity().setFragment(FragmentName.HOME);
    }

    private String parsearAccesibilidadTextoNumero(String accesMin){
        String accTemp;
        switch (accesMin){
            case "ACCESIBLE":
                accTemp="A";
                break;
            case "ACCESIBLE CON DIFICULTAD":
                accTemp="B";
                break;
            case "PRACTICABLE CON AYUDA":
                accTemp="C";
                break;
            case "CUALQUIERA":
                accTemp="D";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return accTemp;
    }


}