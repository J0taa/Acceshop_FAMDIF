package com.example.famdif_final;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference databaseReference;

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

        return view;

    }
}
