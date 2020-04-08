package com.example.projetassociation.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.projetassociation.Utilities.Functions;
import com.example.projetassociation.Utilities.ServiceWeb;
import com.example.projetassociation.Utilities.Session;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    ViewSwitcher viewSwitcher;

    Button btnModifier;
    Button btnValider;
    Button btnAnnuler;
    Button btnCrediterCompte;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialisation des widgets
        initWidgets(view);

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

                // on enregistre en bdd via le service web
                callUpdateAdherent(email, telephone, password, "");

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

        btnCrediterCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomAlertDialog();
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

    private void initWidgets(View view) {
        txtNom = view.findViewById(R.id.txtNom);
        txtPrenom = view.findViewById(R.id.txtPrenom);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtMobile = view.findViewById(R.id.txtMobile);
        txtSolde = view.findViewById(R.id.txtSolde);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtTelephone = view.findViewById(R.id.edtTelephone);
        edtPassword = view.findViewById(R.id.edtPassword);

        viewSwitcher = view.findViewById(R.id.viewswitcher);

        btnModifier = view.findViewById(R.id.btnModifier);
        btnValider = view.findViewById(R.id.btnValider);
        btnAnnuler = view.findViewById(R.id.btnAnnuler);
        btnCrediterCompte = view.findViewById(R.id.btnCrediterCompte);
    }

    private void openCustomAlertDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Créditer compte");

        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.custom_crediter_compte, null);

        final EditText txtMontant = view.findViewById(R.id.txtMontant);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Valider",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // on récupère le montant
                        String montant = txtMontant.getText().toString().trim();
                        // maj de l'objet adhérent
                        adherent.setSolde(adherent.getSolde() + Double.parseDouble(montant));
                        // on enregistre en session
                        Session.setAdherent(adherent);

                        // maj en bdd
                        callUpdateAdherent("", "", "", montant);

                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void callUpdateAdherent(String email, String telephone, String password, String solde) {
        ServiceWeb.callUpdateAdherent(Session.getId(), email, telephone, password, solde, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String retourServiceWeb = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful()) {
                            Functions.getToast(getActivity(), "Erreur service web (code " + response.code() + ")");
                            return;
                        }


                    }
                });
            }
        });
    }
}
