package com.example.famdif_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

public class AccederFragment extends BaseFragment {
    private EditText email;
    private  EditText pass;
    private Button btn;
    private Button btnGoogle;

    private static final int RC_SIGN_IN=100;
    private GoogleSignInClient googleSignInClient;


    public  AccederFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("212697632833-iuh76jl2d2mki6t390nilijb0c4ljiru.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        setMainActivity((MainActivity) getActivity());


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_acceder, container, false);

        email = view.findViewById(R.id.accederTextEmail);
        pass = view.findViewById(R.id.accederTextPass);

        btn = view.findViewById(R.id.accederBtnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInClick(view);
            }
        });

        btnGoogle = view.findViewById(R.id.accederBtnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });


        getMainActivity().getSupportActionBar().setTitle("LOGIN");
        getMainActivity().changeMenu(MenuType.DISCONNECTED);
        getMainActivity().setOptionMenu(R.id.item_log_in);

        return view;
    }

    private void logInClick(View v) {
        try {
            MainActivity.mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            MainActivity.db.collection("users").document(email.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if(ds.get("admin").toString().contains("1")) {
                                        getMainActivity().changeMenu(MenuType.ADMIN_LOGGED);
                                        Controlador.getInstance().setAdmin(1);
                                    }else
                                        getMainActivity().changeMenu(MenuType.USER_LOGGED);
                                }
                            });
                            getMainActivity().getSupportActionBar().setTitle("HOME");
                            getMainActivity().clearBackStack();
                            getMainActivity().setFragment(FragmentName.HOME);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Usuario inexistente o contraseña incorrecta. Por favor, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(accountTask);
                //firebaseAutWithGoogleAccount(account);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> accountTask){
        try {
            GoogleSignInAccount account = accountTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            getMainActivity().setFragment(FragmentName.HOME);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.i("signInResult:failed code=",e.getMessage());
        }

    }

    private void firebaseAutWithGoogleAccount(GoogleSignInAccount account){
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            MainActivity.mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = MainActivity.mAuth.getCurrentUser();

                        }

                    });

        }catch (Exception e){
            e.getMessage();
        }


    }

}