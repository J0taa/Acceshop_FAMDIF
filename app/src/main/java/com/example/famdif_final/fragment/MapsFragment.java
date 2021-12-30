package com.example.famdif_final.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.adaptador.MyInfoWindowAdapter;
import com.example.famdif_final.R;
import com.example.famdif_final.Tienda;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends BaseFragment {

    private List<Tienda> listaAux=new ArrayList<>();
    private List<Tienda> lista=new ArrayList<>();
    private List<Tienda> borrar=new ArrayList<>();
    private String snippet;
    private int result;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            for (Tienda t: Controlador.getInstance().getShops()) {
                    lista.add(t);
            }

            result=lista.size();
            Controlador.getInstance().getShops().clear();



            getMainActivity().getSupportActionBar().setTitle("BUSQUEDA - RESULTADOS ("+result+")");

            for(Tienda t: lista) {
                if(t.getClasificacion()!=null && t.getLatitud()!=null && t.getLongitud()!=null) {
                    LatLng sydney = new LatLng(Double.valueOf(t.getLatitud()), Double.valueOf(t.getLongitud()));

                    snippet = t.getDireccion() + "\n" + "\n" + t.getTipo() + " (" + t.getSubtipo() + ")" + "\n" + "\n" +
                            t.getClasificacion()+" - "+parsearAccesibilidadBBDD(t.getClasificacion()) + "\n";

                    //googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    Log.i("ID - CLASIFICACION ", t.getId() + " " + t.getClasificacion());
                    switch (t.getClasificacion()) {
                        case "A":
                            //googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon();
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(bitmapDescriptorFromVector(getMainActivity(), R.drawable.ic_marcador_a)));
                            break;
                        case "B":
                            //googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(bitmapDescriptorFromVector(getMainActivity(), R.drawable.ic_marcador_b)));

                            break;
                        case "C":
                            //googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(bitmapDescriptorFromVector(getMainActivity(), R.drawable.ic_marcador_c)));
                            break;
                        case "D":
                            //googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(bitmapDescriptorFromVector(getMainActivity(), R.drawable.ic_marcador_d)));
                            break;
                        default:
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(t.getNombre()).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            break;
                    }

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f));
                    googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getContext()));
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            for (Tienda t : lista) {
                                if (t.getNombre().matches(marker.getTitle()))
                                    Controlador.getInstance().setSelectedShop(t);
                            }
                            getMainActivity().setFragment(FragmentName.SEARCH_RESULT_DETAILS);
                        }
                    });
                }
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private String parsearAccesibilidadBBDD(String accesMin){
        String cadena="";
        switch (accesMin){
            case "A":
                cadena="ACCESIBLE";
                break;
            case "B":
                cadena="ACCESIBLE CON DIFICULTAD";
                break;
            case "C":
                cadena="PRACTICABLE CON AYUDA";
                break;
            case "D":
                cadena="MALA ACCESIBILIDAD";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + accesMin);
        }
        return cadena;
    }

    private void limpiaLista(List<Tienda> listaTiendasAux){
        for(Tienda t :listaTiendasAux){
            listaTiendasAux.remove(t);
        }
    }

}