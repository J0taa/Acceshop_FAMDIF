package com.example.famdif_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
        getSugerencias(view);

        return view;
    }
    /*
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
        });*/


        private void getSugerencias(View view){
            Query q=MainActivity.databaseReference.child("suggestions").orderByChild("autor").equalTo(MainActivity.mAuth.getCurrentUser().getEmail().toString());
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dato: snapshot.getChildren()){
                        titulosSugerencias.add("Titulo: "+dato.child("titulo").getValue().toString()+"\n"+"\n"
                                +"-Autor: "+dato.child("autor").getValue().toString()+"\n"+"\n"
                                +"-Detalles: "+dato.child("cuerpo").getValue().toString()+"\n"+"\n");
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


}