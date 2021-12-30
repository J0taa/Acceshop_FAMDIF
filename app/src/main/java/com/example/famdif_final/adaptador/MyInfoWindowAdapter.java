package com.example.famdif_final.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.famdif_final.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;

    public MyInfoWindowAdapter(Context context){
        this.context=context;
    }

    public View getInfoWindow(Marker marker) {
        View v = LayoutInflater.from(context).inflate(R.layout.info_window_adapter, null);
        TextView titulo=v.findViewById(R.id.mNombreTienda);
        TextView snippet=v.findViewById(R.id.mSnipet);
        titulo.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return v;
    }

    public View getInfoContents(Marker marker) {
        View v = LayoutInflater.from(context).inflate(R.layout.info_window_adapter, null);
        TextView titulo=v.findViewById(R.id.mNombreTienda);
        TextView snippet=v.findViewById(R.id.mSnipet);
        titulo.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return v;
    }

}
