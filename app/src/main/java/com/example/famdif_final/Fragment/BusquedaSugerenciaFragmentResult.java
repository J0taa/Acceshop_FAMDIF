package com.example.famdif_final.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.famdif_final.Adaptador.AdaptadorSugerencia;
import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Sugerencia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BusquedaSugerenciaFragmentResult extends BaseFragment{
    private View view;
    private ListView lista;
    private AdaptadorSugerencia adaptador;
    private ArrayList<Sugerencia> model = new ArrayList<>();


    public BusquedaSugerenciaFragmentResult() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        adaptador = new AdaptadorSugerencia(getContext(),model);

        view=inflater.inflate(R.layout.fragment_all_suggestions, container, false);

        recuperarSugerenciasTotal();

        lista=view.findViewById(R.id.idListaSugerenciasUsuario);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                confirmarBorrado(i);
            }
        });

        getMainActivity().getSupportActionBar().setTitle("VER SUGERENCIAS");


        return view;
    }

    public void recuperarSugerenciasTotal(){
        adaptador.notifyDataSetChanged();
        MainActivity.db.collection("suggestions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Sugerencia s = doc.toObject(Sugerencia.class);
                                if(s.getAutor().toLowerCase().contains(Controlador.getInstance().getSugerenciasUsuario().toLowerCase())) {
                                    model.add(s);
                                    adaptador.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    private void confirmarBorrado(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Esta seguro que desea borrar la siguiente sugerencia?"+ "\n"+"\n" +"Titulo: "+model.get(i).getTitulo()+"\n"+"Email: "+model.get(i).getEmail()+"\n"+"\n"+"Esta operación no se puede deshacer" )
                .setTitle(R.string.dialog_title_borrar_sigerencia);
        Sugerencia s = new Sugerencia(model.get(i).getAutor(),model.get(i).getCuerpo(),model.get(i).getTitulo(),model.get(i).getEmail());
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inicioBorrado(s);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void inicioBorrado(Sugerencia sugerencia) {
        MainActivity.db.collection("suggestions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Sugerencia s = document.toObject(Sugerencia.class);
                                if (s.getEmail().matches(sugerencia.getEmail()) && s.getTitulo().matches(sugerencia.getTitulo()))
                                    MainActivity.db.collection("suggestions")
                                            .document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.i("BORRADO", "Sugerencia borrada");
                                                    Toast.makeText(getContext(), "Sugerencia borrada correctamente", Toast.LENGTH_LONG).show();
                                                    getMainActivity().setFragment(FragmentName.VIEW_SUGGESTIONS_RESULT);
                                                    getMainActivity().clearBackStack();
                                                }
                                            });
                            }
                        }
                    }
                });

    }
}
