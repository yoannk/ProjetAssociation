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

    Adherent adherent;

    TextView txtNom;
    TextView txtPrenom;
    TextView txtEmail;
    TextView txtMobile;
    TextView txtSolde;

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
        txtEmail = view.findViewById(R.id.txtEmail);
        txtMobile = view.findViewById(R.id.txtMobile);
        txtSolde = view.findViewById(R.id.txtSolde);

        return view;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    @Override
    public void onResume() {
        super.onResume();

        txtNom.setText("Votre nom : " + adherent.getNom());
        txtPrenom.setText("Votre prénom : " + adherent.getPrenom());
        txtEmail.setText("Votre email : " + adherent.getEmail());
        txtMobile.setText("Votre mobile : " + adherent.getTelephone());
        txtSolde.setText("Votre solde : " + adherent.getSolde() + " €");
    }
}
