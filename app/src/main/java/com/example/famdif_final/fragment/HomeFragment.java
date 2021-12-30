package com.example.famdif_final.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.famdif_final.FragmentName;
import com.example.famdif_final.MainActivity;
import com.example.famdif_final.R;

public class HomeFragment extends BaseFragment {
    Boolean pulsado=false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setMainActivity((MainActivity) getActivity());

        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        getMainActivity().getSupportActionBar().setTitle("HOME");

        Button search = view.findViewById(R.id.homeSearchButton);
        Button suggest = view.findViewById(R.id.homeSuggestionsButton);
        Button mySuggest = view.findViewById(R.id.homeMySuggestionsButton);
        Button rate = view.findViewById(R.id.homeRateShopsButton);
        Button logout = view.findViewById(R.id.homeLogOutButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.SEARCH);
            }
        });

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.SUGGESTIONS);
            }
        });

        mySuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.MY_SUGGESTIONS);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().setFragment(FragmentName.RATE_SHOP);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivity().logOut();
            }
        });


        return view;
    }

}