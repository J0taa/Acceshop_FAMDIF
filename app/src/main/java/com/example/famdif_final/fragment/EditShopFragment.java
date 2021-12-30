package com.example.famdif_final.fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.SubtipoTienda;
import com.example.famdif_final.Tienda;
import com.example.famdif_final.TipoTienda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EditShopFragment extends BaseFragment {

    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;

    private TipoTienda tipoTiendaLista;
    private SubtipoTienda subtipoTiendaLista;
    private Spinner tipoTienda;
    private Spinner subtipoTienda;
    private Spinner despAccesibilidad;
    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private Tienda tienda;

    //private EditText identificador;
    private EditText nombreTienda;
    private EditText direccion;
    private EditText acceso;
    private EditText puertaAcceso;

    private ImageView imagen1;
    private ImageView imagen2;
    private Uri mImageUri;
    private Uri mImageUri1;

    private Button btnImagen1;
    private Button btnImagen2;
    private Button btnAplicarCambios;

    private List<String> accesibilidad= Arrays.asList("A-ACCESIBLE", "B-ACCESIBLE CON DIFICULTAD","C-PRACTICABLE CON AYUDA","D-CUALQUIERA");


    public EditShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());
        tienda= Controlador.getInstance().getSelectedShop();
        final View view = inflater.inflate(R.layout.fragment_edit_shop, container, false);
        tipoTienda=view.findViewById(R.id.desplegableTipoEstablecimiento);
        subtipoTienda=view.findViewById(R.id.desplegablesubtipoEstablecimiento);
        despAccesibilidad=view.findViewById(R.id.desplegableAccesibilidad);
        acceso=view.findViewById(R.id.editarTiendaAcceso);
        puertaAcceso=view.findViewById(R.id.editarTiendaPuertaAcceso);

        btnImagen1=view.findViewById(R.id.btnEditarImg1);
        btnImagen2=view.findViewById(R.id.btnEditarImg2);
        btnAplicarCambios=view.findViewById(R.id.btnGuardar);

        tipoTiendaLista = new TipoTienda();

        subtipoTiendaLista = new SubtipoTienda(tienda.getTipo());


        adapt = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,tipoTiendaLista.getTiposTienda());
        tipoTienda.setAdapter(adapt);
        tipoTienda.setSelection(getIndex(tipoTienda, tienda.getTipo()));

        adapt1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtipoTiendaLista.getSubTiposTienda());
        subtipoTienda.setAdapter(adapt1);
        subtipoTienda.setSelection(getIndex(subtipoTienda,tienda.getSubtipo()));

        adapt2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,accesibilidad);
        despAccesibilidad.setAdapter(adapt2);
        despAccesibilidad.setSelection(getIndex(despAccesibilidad, parsearAccesibilidadBBDD(tienda.getClasificacion())));

        nombreTienda=view.findViewById(R.id.textoNombreTienda);
        nombreTienda.setText(tienda.getNombre());
        direccion=view.findViewById(R.id.textoDireccion);
        direccion.setText(tienda.getDireccion());
        acceso.setText(tienda.getAcceso());
        puertaAcceso.setText(tienda.getPuertaAcceso());

        imagen1=view.findViewById(R.id.image_view_upload);
        imagen2=view.findViewById(R.id.image_view_upload1);

        obtenerImagen();

        btnImagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }

        });

        btnImagen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser2();
            }
        });

        btnAplicarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea confirmar los cambios indicados?");
                builder.setTitle("CONFIRMAR ACTUALIZACION TIENDA");
                builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        borrarAnterior();
                        actualizarDatosTienda();
                        uploadFile();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void actualizarDatosTienda() {
        Log.i("Tienda a actualizar:",tienda.getId());
        if(nombreTienda.getText().toString().length()<=0){
            Toast.makeText(getContext(),"El nombre de la tienda no puede ser vacio",Toast.LENGTH_LONG).show();
        }else if(direccion.getText().toString().length()<=0){
                Toast.makeText(getContext(),"LA dirección no puede ser vacía",Toast.LENGTH_LONG).show();
        }else{
                MainActivity.db.collection("comerciosElCarmenTest").document(tienda.getId())
                        .update("nombre",nombreTienda.getText().toString(),
                                "direccion",direccion.getText().toString(),
                                "tipo",tipoTienda.getSelectedItem().toString(),
                                "subtipo",subtipoTienda.getSelectedItem().toString(),
                                "acceso",acceso.getText().toString(),
                                "puertaAcceso",puertaAcceso.getText().toString(),
                                "clasificacion",despAccesibilidad.getSelectedItem().toString().substring(0,1))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"Tienda actualizada correctamente",Toast.LENGTH_LONG).show();
                                getMainActivity().clearBackStack1();
                                getMainActivity().setFragment(FragmentName.SEARCH);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ERROR AL ACTUALIZAR", e.getMessage());
                    }
                });

            }
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
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("/" + tienda.getId() + "_A.jpg");

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

        mStorageReference = FirebaseStorage.getInstance().getReference("/"+tienda.getId()+"_B.jpg");

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
            Picasso.with(this.getContext()).load(mImageUri).into(imagen1);

        }else if(requestCode ==  PICK_IMAGE_2 && resultCode == -1 && data != null && data.getData()!=null ) {
            mImageUri1 = data.getData();
            Picasso.with(this.getContext()).load(mImageUri1).into(imagen2);
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void borrarAnterior(){
        if(mImageUri != null){
            StorageReference storageReference = MainActivity.mStorageRef.child(tienda.getId()+"_A" +
                    "." +getFileExtension(mImageUri));
            storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //BORRADO
                }
            });
        }
        if(mImageUri1!=null){
            StorageReference storageReference = MainActivity.mStorageRef.child(tienda.getId()+"_B" +
                    "." +getFileExtension(mImageUri1));
            storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //BORRADO
                }
            });
        }

    }

    private void uploadFile(){
        if(mImageUri != null){
            StorageReference storageReference = MainActivity.mStorageRef.child(tienda.getId()+"_A" +
                    "." +getFileExtension(mImageUri));
            storageReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });
        }
        if(mImageUri1!=null){
            StorageReference storageReference = MainActivity.mStorageRef.child(tienda.getId()+"_B" +
                    "." +getFileExtension(mImageUri1));
            storageReference.putFile(mImageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Upload upload = new Upload(identificador.getText().toString()+"_B", uri.toString());
                            Controlador.getInstance().setSelectedShop(tienda);
                            getMainActivity().clearBackStack();
                            getMainActivity().setFragment(FragmentName.SEARCH_RESULT_DETAILS);

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


    }

    private String parsearAccesibilidadBBDD(String accesMin){
        String cadena="";
        switch (accesMin){
            case "A":
                cadena="A-ACCESIBLE";
                break;
            case "B":
                cadena="B-ACCESIBLE CON DIFICULTAD";
                break;
            case "C":
                cadena="C-PRACTICABLE CON AYUDA";
                break;
            case "D":
                cadena="D-MALA ACCESIBILIDAD";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return cadena;
    }


}
