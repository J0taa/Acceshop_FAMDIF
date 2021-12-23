package com.example.famdif_final.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.famdif_final.R;
import com.example.famdif_final.Tienda;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdaptadorTienda extends BaseAdapter {
    private Context context;
    private ArrayList<Tienda> listaTienda;

    private double puntMedia;
    private double puntMediaFin;
    private int cont1;
    private double miPuntuacion;
    private Task<QuerySnapshot> task;

    public AdaptadorTienda(Context context, ArrayList<Tienda> lista){
        this.context=context;
        this.listaTienda=lista;
    }

    @Override
    public int getCount() {
        return listaTienda.size();
    }

    @Override
    public Object getItem(int i) {
        return listaTienda.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_tienda_filtrada, null);
        }
        TextView nombreTienda = (TextView) convertView.findViewById(R.id.idNombreTienda);
        TextView direccionTienda = (TextView) convertView.findViewById(R.id.idTextDireccion);


        nombreTienda.setText(listaTienda.get(position).getNombre());
        direccionTienda.setText(listaTienda.get(position).getDireccion());

        return convertView;
    }



}
