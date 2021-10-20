package com.example.famdif_final;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends BaseFragment {

    private List<Tienda>lista=new ArrayList<>();
    private String snippet;
    private String seleccionada;
    private Tienda tiendaAux;
    private int result;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            lista=Controlador.getInstance().getShops();
            result=lista.size();
            getMainActivity().getSupportActionBar().setTitle("BUSQUEDA - RESULTADOS ("+result+")");
            Log.i("Tiendas Encontradas: ",String.valueOf(lista.size()));
            for(Tienda t: lista) {
                LatLng sydney = new LatLng(Double.valueOf(t.getLatitud()),Double.valueOf(t.getLongitud()));
                snippet=t.getDireccion() + "\n" +"\n" + t.getTipo() + " (" + t.getSubtipo()+")" + "\n" + "\n" +
                        t.getAccesibilidad() + "\n";

                switch (t.getAccesibilidad()){
                    case "ACCESIBLE":
                        googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        break;
                    case "ACCESIBLE CON DIFICULTAD":
                        googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        break;
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getContext()));

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for (Tienda t:lista) {
                            if(t.getNombre().matches(marker.getTitle()))
                                Controlador.getInstance().setSelectedShop(t);
                        }
                        getMainActivity().setFragment(FragmentName.SEARCH_RESULT_DETAILS);
                    }
                });
            }

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());
        getMainActivity().getSupportActionBar().setTitle("BUSQUEDA - RESULTADOS ("+result+")");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        return view;
    }

}