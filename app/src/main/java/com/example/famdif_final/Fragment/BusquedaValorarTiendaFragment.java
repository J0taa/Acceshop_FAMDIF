package com.example.famdif_final.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;

public class BusquedaValorarTiendaFragment extends BaseFragment {
    private View view;
    private EditText nombreTienda;
    private EditText direccionTienda;
    private Button btnBusqueda;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_valorar_tienda, container, false);
        nombreTienda=view.findViewById(R.id.textoNombreTienda);
        direccionTienda=view.findViewById(R.id.textoDireccion);
        btnBusqueda=view.findViewById(R.id.btnBusquedaTiendaValorar);

        getMainActivity().getSupportActionBar().setTitle("VALORAR TIENDA");

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarBusqueda();
            }
        });

        return view;
    }

    private void realizarBusqueda() {
        if(nombreTienda.getText().length()==0 && direccionTienda.getText().length()==0){
            Toast.makeText(getContext(),"Es necesario introducir nombre o dirección",Toast.LENGTH_LONG).show();
        }else if(nombreTienda.getText().length()>0 && direccionTienda.getText().length()==0){
            Controlador.getInstance().setNombreTiendaValorar(nombreTienda.getText().toString());
            getMainActivity().setFragment(FragmentName.RATE_SHOP_RESULT);

        }else if(nombreTienda.getText().length()==0 && direccionTienda.getText().length()>0){
            Controlador.getInstance().setDireccionTiendaValorar(direccionTienda.getText().toString());
            getMainActivity().setFragment(FragmentName.RATE_SHOP_RESULT);
        }else{
            Controlador.getInstance().setDireccionTiendaValorar(direccionTienda.getText().toString());
            Controlador.getInstance().setNombreTiendaValorar(nombreTienda.getText().toString());
            getMainActivity().setFragment(FragmentName.RATE_SHOP_RESULT);
        }

    }


}
