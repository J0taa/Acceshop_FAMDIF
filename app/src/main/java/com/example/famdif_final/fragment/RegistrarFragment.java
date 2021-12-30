package com.example.famdif_final.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.famdif_final.Controlador;
import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.MenuType;
import com.example.famdif_final.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.famdif_final.R.id.item_sign_in;


public class RegistrarFragment extends BaseFragment {
    private View v;
    private TextInputLayout nombre;
    private TextInputLayout email;
    private TextInputLayout pass;
    private TextInputLayout pass2;
    private Button btnRegistro;
    private TextView errorPass;

    private static final String PASSWORD_PATTERN ="^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

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
        errorPass = view.findViewById(R.id.errorPass);

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

            if(!isValid(pass.getEditText().getText().toString())){
                Toast.makeText(getContext(),"La contraseña no cumple con los criterios establecidos. Por favor, revisar",Toast.LENGTH_LONG).show();
                errorPass.setVisibility(View.VISIBLE);
            }else if(!pass.getEditText().getText().toString().matches(pass2.getEditText().getText().toString())){
                Toast.makeText(getContext(),"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            }
            else{
                MainActivity.mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(),pass.getEditText().getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //REGISTRO FIRECLOUD
                            //Toast.makeText(getContext(), "Bienvenido "+email.getText().toString(), Toast.LENGTH_SHORT).show();
                            getMainActivity().getSupportActionBar().setTitle("HOME");
                            getMainActivity().clearBackStack();
                            getMainActivity().changeMenu(MenuType.USER_LOGGED);
                            getMainActivity().setFragment(FragmentName.HOME);
                            Controlador.getInstance().setUsuario(email.getEditText().getText().toString());

                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre",nombre.getEditText().getText().toString());
                            user.put("email",email.getEditText().getText().toString());
                            user.put("pass",pass.getEditText().getText().toString());
                            user.put("admin","0");
                            MainActivity.db.collection("users").document(email.getEditText().getText().toString())
                                    .set(user);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getMainActivity(), "El correo indicado ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR LOGIN: ",e.getMessage());
                    }
            });
            }

        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT);
        }
    }

    public boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}