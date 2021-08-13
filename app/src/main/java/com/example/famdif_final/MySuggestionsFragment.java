package com.example.famdif_final;

import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySuggestionsFragment extends BaseFragment {
    private ListView listaSugerencias;
    private List<String> titulosSugerencias=new ArrayList<>();
    private ArrayAdapter adapter;

    private static final String TAG = "MyActivity";
    private String cadena;


    public MySuggestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        final View view=inflater.inflate(R.layout.fragment_my_suggestions, container, false);
        listaSugerencias=view.findViewById(R.id.idLista);
        adapter= new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, titulosSugerencias);
        listaSugerencias.setAdapter(adapter);

        getMainActivity().getSupportActionBar().setTitle("MIS SUGERENCIAS");
        getMainActivity().changeMenu(MenuType.USER_LOGGED);
        getMainActivity().setOptionMenu(R.id.item_suggestions);
        recuperarSugerencias(view);
        return view;
    }

    private void recuperarSugerencias(View view){
        //MainActivity.db.collection("suggestions").document(MainActivity.mAuth.getCurrentUser().getEmail().toString())
        MainActivity.db.collection("suggestions").whereEqualTo("autor",MainActivity.mAuth.getCurrentUser().getEmail().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    Log.i(TAG, doc.getId() + " => " + doc.getData());
                    Log.i(TAG, "El título de la sugerencia es: "+doc.getString("titulo"));
                    titulosSugerencias.add("-Titulo: "+doc.getString("titulo").toString()+"\n"+"\n"
                            +"-Autor: "+doc.getString("autor")+"\n"+"\n"
                            +"-Detalles: "+doc.getString("cuerpo"));
                    adapter.notifyDataSetChanged();
                }

            }
        });


    }

}