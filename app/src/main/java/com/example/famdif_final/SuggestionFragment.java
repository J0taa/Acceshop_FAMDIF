package com.example.famdif_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class SuggestionFragment extends BaseFragment {
    private EditText titulo;
    private EditText cuerpo;
    private Button btnEnviar;

    public SuggestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        titulo = view.findViewById(R.id.textoTitulo);
        cuerpo = view.findViewById(R.id.mensajeCuerpo);

        btnEnviar = view.findViewById(R.id.btnEnviarSugerencia);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSugerencia(v);
            }
        });

        getMainActivity().getSupportActionBar().setTitle("ENVIAR SUGERENCIA");
        getMainActivity().changeMenu(MenuType.USER_LOGGED);
        getMainActivity().setOptionMenu(R.id.item_suggestions);
        return view;

    }

    private void enviarSugerencia(View view) {
        Map<String, Object> suggestion = new HashMap<>();
        suggestion.put("autor",MainActivity.mAuth.getCurrentUser().getEmail().toString());
        suggestion.put("titulo",titulo.getText().toString());
        suggestion.put("cuerpo",cuerpo.getText().toString());

        MainActivity.databaseReference.child("suggestions").child(String.valueOf(suggestion.hashCode())).setValue(suggestion);


    }
}