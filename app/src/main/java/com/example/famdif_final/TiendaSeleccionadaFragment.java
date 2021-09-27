package com.example.famdif_final;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TiendaSeleccionadaFragment extends BaseFragment {

    private Tienda tiendaSeleccionada = Controlador.getInstance().getSelectedShop();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tienda_seleccionada, container, false);
        getMainActivity().getSupportActionBar().setTitle("Búsqueda - "+Controlador.getInstance().getSelectedShop().getNombre());
        Log.i("TiendaSeleccionada:",tiendaSeleccionada.getNombre());
        return view;

    }
}
