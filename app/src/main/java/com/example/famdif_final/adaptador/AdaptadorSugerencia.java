package com.example.famdif_final.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.famdif_final.R;
import com.example.famdif_final.Sugerencia;

import java.util.ArrayList;

public class AdaptadorSugerencia extends BaseAdapter {
    private Context context;
    private ArrayList<Sugerencia> listaSugerencias;

    public AdaptadorSugerencia(Context context, ArrayList<Sugerencia> lista){
        this.context=context;
        this.listaSugerencias=lista;
    }

    @Override
    public int getCount() {
        return listaSugerencias.size();
    }

    @Override
    public Object getItem(int i) {
        return listaSugerencias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sugerencia, null);
        }
        TextView autor = (TextView) convertView.findViewById(R.id.idNombreUsuario);
        TextView email = (TextView) convertView.findViewById(R.id.idEmailUsuario);
        TextView cuerpo = (TextView) convertView.findViewById(R.id.idCuerpoSugerencia);
        TextView titulo = (TextView) convertView.findViewById(R.id.idTituloSugerencia);

        autor.setText(listaSugerencias.get(position).getAutor());
        email.setText(listaSugerencias.get(position).getEmail());
        titulo.setText(listaSugerencias.get(position).getTitulo());
        cuerpo.setText(listaSugerencias.get(position).getCuerpo());

        return convertView;
    }
}
