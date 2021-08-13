package com.example.famdif_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class IndexFragment extends BaseFragment {
    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());

        View view = inflateFragment(R.layout.fragment_index, inflater, container);

        Button search = view.findViewById(R.id.indexSearchButton);
        Button signIn = view.findViewById(R.id.indexSignInButton);
        Button logIn = view.findViewById(R.id.indexLogInButton);
        Button aboutUs = view.findViewById(R.id.indexAboutUsButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.SEARCH);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.SIGN_IN);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.LOG_IN);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.ABOUT_US);
            }
        });

        getMainActivity().getSupportActionBar().setTitle(R.string.app_name);
        getMainActivity().changeMenu(MenuType.DISCONNECTED);
        // Para que se marque la opcion del menu cuando hago un atras
        getMainActivity().setOptionMenu(R.id.item_index);

        return view;
    }

}