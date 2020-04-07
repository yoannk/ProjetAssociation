package com.example.projetassociation.Fragments;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.R;
import com.example.projetassociation.Utilities.Session;

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

    EditText edtEmail;
    EditText edtTelephone;
    EditText edtPassword;

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

        edtEmail = view.findViewById(R.id.edtEmail);
        edtTelephone = view.findViewById(R.id.edtTelephone);
        edtPassword = view.findViewById(R.id.edtPassword);

        final ViewSwitcher viewSwitcher = view.findViewById(R.id.viewswitcher);

        Button btnModifier = view.findViewById(R.id.btnModifier);
        Button btnValider = view.findViewById(R.id.btnValider);
        Button btnAnnuler = view.findViewById(R.id.btnAnnuler);

        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on bascule sur notre autre vue - mode EditText
                viewSwitcher.showNext();
            }
        });

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enregistrer en bdd
                String email = edtEmail.getText().toString().trim();
                String telephone = edtTelephone.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // maj de l'adherent
                adherent.setEmail(email);
                adherent.setTelephone(telephone);

                // maj objet adherent en session
                Session.setAdherent(adherent);

                setData();

                // on bascule sur la vue précédente - mode TextView
                viewSwitcher.showPrevious();
            }
        });

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showPrevious();
            }
        });

        return view;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    @Override
    public void onResume() {
        super.onResume();

        setData();
    }

    private void setData() {
        txtNom.setText("Votre nom : " + adherent.getNom());
        txtPrenom.setText("Votre prénom : " + adherent.getPrenom());
        txtEmail.setText("Votre email : " + adherent.getEmail());
        txtMobile.setText("Votre mobile : " + adherent.getTelephone());
        txtSolde.setText("Votre solde : " + adherent.getSolde() + " €");

        edtEmail.setText(adherent.getEmail());
        edtTelephone.setText(adherent.getTelephone());
        edtPassword.setText("");
    }
}
