package com.example.famdif_final.Fragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class BusquedaSugerenciaFragment extends BaseFragment {
    private EditText nombreBuscar;
    private Button btnBusqueda;


    public BusquedaSugerenciaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        final View view=inflater.inflate(R.layout.fragment_busqueda_sugerencia, container, false);
        getMainActivity().getSupportActionBar().setTitle("VER SUGERENCIAS");

        nombreBuscar=view.findViewById(R.id.buscarSugerencia);
        btnBusqueda=view.findViewById(R.id.btnBuscarSugerencia);

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nombreBuscar.getText().toString().equals("")) {
                    MainActivity.db.collection("users")
                            .whereEqualTo("nombre",nombreBuscar.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().isEmpty()){
                                            Toast.makeText(getContext(),"El usuario buscado no existe",Toast.LENGTH_LONG).show();
                                        }else{
                                            Controlador.getInstance().setSugerenciasUsuario(nombreBuscar.getText().toString().toLowerCase());
                                            getMainActivity().setFragment(FragmentName.VIEW_SUGGESTIONS_RESULT);
                                        }
                                    }
                                }
                            });


                }
            }
        });


        return view;
    }

}
