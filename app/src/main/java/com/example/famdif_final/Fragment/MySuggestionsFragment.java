package com.example.famdif_final.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.famdif_final.Adaptador.AdaptadorSugerencia;
import com.example.famdif_final.Fragment.BaseFragment;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Sugerencia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MySuggestionsFragment extends BaseFragment {
    private View view;
    private ListView lista;
    private AdaptadorSugerencia adaptador;
    private ArrayList<Sugerencia> model = new ArrayList<>();
    private ArrayList<Sugerencia> aux = new ArrayList<>();


    public MySuggestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        adaptador = new AdaptadorSugerencia(getContext(),model);

        view=inflater.inflate(R.layout.fragment_my_suggestions, container, false);

        recuperarSugerencias();

        lista=view.findViewById(R.id.idListaSugerencias);
        lista.setAdapter(adaptador);

        getMainActivity().getSupportActionBar().setTitle("MIS SUGERENCIAS");


        return view;
    }

    public void recuperarSugerencias(){
        MainActivity.db.collection("suggestions")
                .whereEqualTo("email",MainActivity.mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Sugerencia s = doc.toObject(Sugerencia.class);
                                model.add(s);
                                adaptador.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}