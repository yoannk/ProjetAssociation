package com.example.projetassociation.Fragments;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView txtNom;
    TextView txtPrenom;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtNom = view.findViewById(R.id.txtNom);
        txtPrenom = view.findViewById(R.id.txtPrenom);

        return view;
    }

    public void setAdherent(Adherent adherent) {
        txtNom.setText("Votre nom : " + adherent.getNom());
        txtPrenom.setText("Votre prénom : " + adherent.getPrenom());
    }
}
