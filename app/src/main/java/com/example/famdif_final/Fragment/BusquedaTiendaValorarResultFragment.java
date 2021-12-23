package com.example.famdif_final.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.famdif_final.Adaptador.AdaptadorTienda;
import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Tienda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BusquedaTiendaValorarResultFragment extends BaseFragment{
    private View view;
    private ListView lista;
    private AdaptadorTienda adaptador;
    private ArrayList<Tienda> model = new ArrayList<>();
    private String nombreTiendaAux;
    private String direccionAux;


    public BusquedaTiendaValorarResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        adaptador = new AdaptadorTienda(getContext(),model);

        view=inflater.inflate(R.layout.fragment_lista_tiendas_valorar, container, false);

        recuperarTiendas();

        lista=view.findViewById(R.id.idListaTiendaValorar);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                detallesTienda(i);
            }
        });

        getMainActivity().getSupportActionBar().setTitle("VALORAR TIENDA");


        return view;
    }

    private void recuperarTiendas() {
        model.clear();
        MainActivity.db.collection("comerciosElCarmenTest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(Controlador.getInstance().getNombreTiendaValorar().length()>0 && Controlador.getInstance().getDireccionTiendaValorar().length()>0){
                                for (QueryDocumentSnapshot d : task.getResult()){
                                    if(d.get("direccion").toString().contains(Controlador.getInstance().getDireccionTiendaValorar()) && d.get("nombre").toString().contains(Controlador.getInstance().getNombreTiendaValorar())) {
                                        Tienda t = d.toObject(Tienda.class);
                                        model.add(t);
                                        adaptador.notifyDataSetChanged();
                                    }
                                }
                            }else if(Controlador.getInstance().getDireccionTiendaValorar().length()>0){
                                for (QueryDocumentSnapshot d : task.getResult()){
                                    if(d.get("direccion").toString().contains(Controlador.getInstance().getDireccionTiendaValorar())) {
                                        Tienda t = d.toObject(Tienda.class);
                                        model.add(t);
                                        adaptador.notifyDataSetChanged();
                                    }
                                }
                            }else{
                                for (QueryDocumentSnapshot d : task.getResult()){
                                    if(d.get("nombre").toString().contains(Controlador.getInstance().getNombreTiendaValorar())) {
                                        Tienda t = d.toObject(Tienda.class);
                                        model.add(t);
                                        adaptador.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void detallesTienda(int i){
        Tienda t = new Tienda(model.get(i).getId(),model.get(i).getNombre(),model.get(i).getTipo(),model.get(i).getSubtipo(),
                model.get(i).getDireccion(),model.get(i).getClasificacion(),model.get(i).getLatitud(),model.get(i).getLongitud(),
                model.get(i).getFechaVisita(),model.get(i).getAcceso(),model.get(i).getPuertaAcceso());

        Controlador.getInstance().setSelectedShop(t);
        getMainActivity().setFragment(FragmentName.SEARCH_RESULT_DETAILS);
    }

}

