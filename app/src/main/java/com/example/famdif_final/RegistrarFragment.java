package com.example.famdif_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;

import static com.example.famdif_final.MainActivity.databaseReference;
import static com.example.famdif_final.R.id.item_sign_in;


public class RegistrarFragment extends BaseFragment {
    private View v;
    private TextView nombre;
    private TextView email;
    private TextView pass;
    private TextView pass2;
    private Button btnRegistro;
    //private FirebaseAuth mAuth;

    public RegistrarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());
        //mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_registrar, container, false);
        nombre = view.findViewById(R.id.registroTextNombre);
        email = view.findViewById(R.id.registroTextEmail);
        pass = view.findViewById(R.id.registroTextPass);
        pass2 = view.findViewById(R.id.registroTextPass2);

        btnRegistro = view.findViewById(R.id.registroBtnRegistrar);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClick(view);
            }
        });

        getMainActivity().getSupportActionBar().setTitle("Registrar");
        getMainActivity().changeMenu(MenuType.DISCONNECTED);
        getMainActivity().setOptionMenu(item_sign_in);

        return view;
    }

    private void signInClick(View view) {
        try {
            MainActivity.mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        private static final String TAG = "PRUEBAS";

                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //REGISTRO FIRECLOUD
                            //Toast.makeText(getContext(), "Bienvenido "+email.getText().toString(), Toast.LENGTH_SHORT).show();
                            getMainActivity().getSupportActionBar().setTitle("HOME");
                            getMainActivity().clearBackStack();
                            getMainActivity().changeMenu(MenuType.USER_LOGGED);
                            getMainActivity().setFragment(FragmentName.HOME);

                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre",nombre.getText().toString());
                            user.put("email",email.getText().toString());
                            user.put("pass",pass.getText().toString());
                            user.put("admin","0");
                            //MainActivity.db.collection("users").document(email.getText().toString())
                                    //.set(user);

                            //REGISTRO NUEVA BBDD
                           String ident=MainActivity.mAuth.getCurrentUser().getUid();
                           databaseReference.child("users").child(ident).setValue(user);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getMainActivity(), "Usuario inexistente o contraseña incorrecta. Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT);
        }
    }

}