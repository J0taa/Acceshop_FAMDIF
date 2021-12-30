package com.example.famdif_final.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BusquedaUsuarioFragment extends BaseFragment {

    private EditText nombreBuscar;
    private Button btnBusqueda;


    public BusquedaUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        final View view=inflater.inflate(R.layout.fragment_busqueda_usuario, container, false);
        getMainActivity().getSupportActionBar().setTitle("BORRAR USUARIO");

        nombreBuscar=view.findViewById(R.id.buscarUsuario);
        btnBusqueda=view.findViewById(R.id.btnBuscarUsuario);

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Usuario usuario = document.toObject(Usuario.class);
                            if(usuario.getNombre().contains(nombreBuscar.getText().toString())){
                                Controlador.getInstance().getUsuarios().add(usuario);
                            }
                        }
                        if(Controlador.getInstance().getUsuarios().isEmpty()){
                            Toast.makeText(getContext(), "No se ha encontrado el usuario", Toast.LENGTH_LONG).show();
                        }else {
                            getMainActivity().setFragment(FragmentName.LIST_USUARIOS_BORRAR);
                        }
                    }
                });
            }
        });


        return view;
    }
}
