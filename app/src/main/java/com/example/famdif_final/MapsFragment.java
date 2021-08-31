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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends BaseFragment {

    private List<Tienda>lista=new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            lista=Controlador.getInstance().getShops();
            for(Tienda t: lista) {
                Log.i("LATITUD",t.getLatitud());
                Log.i("LONGITUD",t.getLongitud());
                LatLng sydney = new LatLng(Double.valueOf(t.getLatitud()),Double.valueOf(t.getLongitud()));
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marcador de prueba"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }


        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        return view;
    }

}