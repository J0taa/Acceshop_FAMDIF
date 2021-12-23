package com.example.famdif_final.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.famdif_final.R;
import com.example.famdif_final.UsuarioFiltrado;

import java.util.ArrayList;

public class AdaptadorUsuario extends BaseAdapter {
    private Context context;
    private ArrayList<UsuarioFiltrado> listaUsuarios;

    public AdaptadorUsuario(Context context, ArrayList<UsuarioFiltrado> lista){
        this.context=context;
        this.listaUsuarios=lista;
    }

    @Override
    public int getCount() {
       return listaUsuarios.size();
    }

    @Override
    public Object getItem(int i) {
        return listaUsuarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_usuario, null);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.idNombreUsuario);
        TextView email = (TextView) convertView.findViewById(R.id.idEmailUsuario);


        nombre.setText(listaUsuarios.get(position).getName());
        email.setText(listaUsuarios.get(position).getMail());


        return convertView;
    }
}
