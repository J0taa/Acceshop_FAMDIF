package com.example.famdif_final;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

public class MySuggestionsFragment extends BaseFragment {
    private ListView listaSugerencias;
    private List<String> titulosSugerencias=new ArrayList<>();
    private ArrayAdapter adapter;


    private static final Spanned autor = Html.fromHtml("<b>Autor: </b>");
    private static final Spanned titulo = Html.fromHtml("<b>Titulo: </b>");
    private static final Spanned detalles = Html.fromHtml("<b>Detalles: </b>");


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
        recuperarSugerencias(view);

        return view;
    }

    private void recuperarSugerencias(View view) {
        MainActivity.db.collection("suggestions").whereEqualTo("autor", MainActivity.mAuth.getCurrentUser().getEmail().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    titulosSugerencias.add(titulo + doc.getString("titulo").toString() + "\n" + "\n"
                            + autor + doc.getString("autor") + "\n" + "\n"
                            + detalles + doc.getString("cuerpo"));
                    adapter.notifyDataSetChanged();

                }

            }
        });
    }


}