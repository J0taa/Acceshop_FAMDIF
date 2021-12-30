package com.example.famdif_final.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.famdif_final.adaptador.AdaptadorUsuario;
import com.example.famdif_final.Controlador;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;
import com.example.famdif_final.Sugerencia;
import com.example.famdif_final.Usuario;
import com.example.famdif_final.UsuarioFiltrado;
import com.example.famdif_final.Votacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListaUsuariosBorrarFragment extends BaseFragment {
    private ListView lista;
    private AdaptadorUsuario adaptador;
    private ArrayList<Usuario> usuariosAux = new ArrayList<>();
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<UsuarioFiltrado> model = new ArrayList<>();
    private static ArrayList<Votacion> listaVotaciones = new ArrayList<>();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());

        listaUsuarios.addAll(Controlador.getInstance().getUsuarios());

        for(Usuario u:listaUsuarios){
            usuariosAux.add(u);
        }

        Controlador.getInstance().getUsuarios().clear();
        listaUsuarios.clear();


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lista_usuarios, container, false);

        obtenerUsuarios();

        adaptador = new AdaptadorUsuario(getContext(),model);
        lista=view.findViewById(R.id.idLista);
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                confirmarBorrado(i);
            }
        });

        getMainActivity().getSupportActionBar().setTitle("BORRAR USUARIO");

        return view;
    }

    private void obtenerUsuarios(){
        for (Usuario u : usuariosAux) {
            UsuarioFiltrado usuarioFiltrado = new UsuarioFiltrado(u.getNombre(), u.getEmail());
            model.add(usuarioFiltrado);
        }
    }

    private void confirmarBorrado(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Esta seguro que desea borrar al siguiente usuario?"+ "\n"+"\n" +"Nombre: "+model.get(i).getName()+"\n"+"Email: "+model.get(i).getMail()+"\n"+"\n"+"Esta operación no se puede deshacer" )
                .setTitle("BORRAR USUARIO");
        UsuarioFiltrado u = new UsuarioFiltrado(model.get(i).getName(),model.get(i).getMail());
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                inicioBorrado(u);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void inicioBorrado(UsuarioFiltrado u) {
        MainActivity.db.collection("votaciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Votacion v = document.toObject(Votacion.class);
                                if(v.getEmail().matches(u.getMail()))
                                    MainActivity.db.collection("votaciones")
                                    .document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("BORRADO","Votaciones borradas");
                                        }
                                    });


                            }
                        }
                    }
                });

        MainActivity.db.collection("suggestions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() & task.getResult()!=null){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Sugerencia s = new Sugerencia(doc.getString("autor"), doc.getString("cuerpo"),doc.getString("titulo"), doc.getString("email"));
                        if(s.getAutor().matches(u.getMail()))
                            MainActivity.db.collection("suggestions")
                            .document(doc.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("BORRADO", "Sugerencias borrada");
                                }
                            });

                    }
                }
            }
        });

        MainActivity.db.collection("users")
                .whereEqualTo("email",u.getMail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() & task.getResult()!=null){
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                MainActivity.db.collection("users")
                                        .document(doc.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i("BORRADO USUARIO","usuario borrado");
                                            }
                                        });
                            }
                        }
                    }
                });
    }


}
