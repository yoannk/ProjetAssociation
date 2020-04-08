package com.example.projetassociation.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetassociation.Entities.Sortie;
import com.example.projetassociation.Entities.Sorties;
import com.example.projetassociation.HomeActivity;
import com.example.projetassociation.R;
import com.example.projetassociation.Utilities.Session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SortiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Context context;
    RecyclerView rcwSorties;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SortiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortiesFragment newInstance(String param1, String param2) {
        SortiesFragment fragment = new SortiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sorties, container, false);

        rcwSorties = view.findViewById(R.id.rcwSorties);

        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<SortieHolder> {

        Sorties sorties;

        public RecyclerViewAdapter(Sorties sorties) {
            this.sorties = sorties;
        }

        @NonNull
        @Override
        public SortieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sortie, parent, false);

            return new SortieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SortieHolder sortieHolder, int position) {
            Sortie sortie = this.sorties.get(position);
            sortieHolder.setSortie(sortie);
        }

        @Override
        public int getItemCount() {
            return sorties.size();
        }
    }

    public class SortieHolder extends RecyclerView.ViewHolder {

        public final TextView txtNom;
        public final TextView txtPrix;
        public final TextView txtDate;
        public final ImageView imgPhoto;
        public final Button btnDetails;
        public final Button btnInscription;

        public SortieHolder(@NonNull View itemView) {
            super(itemView);

            // on initialise les widgets qui sont dans le layout
            txtNom = itemView.findViewById(R.id.txtNom);
            txtPrix = itemView.findViewById(R.id.txtPrix);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            btnDetails = itemView.findViewById(R.id.btnDetail);
            btnInscription = itemView.findViewById(R.id.btnInscription);
        }

        public void setSortie(final Sortie sortie) {
            // le match des données avec les widgets
            txtNom.setText(sortie.getNom());
            txtPrix.setText("" + sortie.getPrix() + " €");
            txtDate.setText("");
            //imgPhoto.setImageDrawable();

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCustomAlertDialog(sortie);
                }
            });

            btnInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // appel du service web au niveau HomeActivity
                    ((HomeActivity) getActivity()).callInscriptionSortieAdherent(sortie.getIdSortie(), sortie.getIdAssociation());
                }
            });
        }
    }

    // appel depuis HomeActivity
    public void loadSorties(Sorties sorties, Context context) {
        this.context = context;
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(sorties);

        // la manière dont les adherents doivent s'afficher
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        // version GridView
        //RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(context, 3);

        // Effet sur le RecyclerView
        //rcvAdherents.setItemAnimator(new DefaultItemAnimator());

        rcwSorties.setLayoutManager(layoutManager);
        rcwSorties.setAdapter(recyclerViewAdapter);
    }

    private void openCustomAlertDialog(final Sortie sortie) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Detail sortie");

        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.custom_detail_sortie, null);

        TextView txtNom = view.findViewById(R.id.txtNom);
        TextView txtDescription = view.findViewById(R.id.txtDescription);
        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtPrix = view.findViewById(R.id.txtPrix);
        TextView txtCapacite = view.findViewById(R.id.txtCapacite);
        TextView txtInscris = view.findViewById(R.id.txtInscris);

        txtNom.setText(sortie.getNom());
        txtDescription.setText(sortie.getDescription());
        txtDate.setText("Date : " + sortie.getDate());
        txtPrix.setText("Prix : " + sortie.getPrix() + " €");
        txtCapacite.setText("Capacité maximum : " + sortie.getCapaciteMaximum());
        txtInscris.setText("Nombre d'inscrits : " + sortie.getNbinscrits());

        ImageView imageView = view.findViewById(R.id.imgPhoto);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Valider",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // appel du service web au niveau HomeActivity
                        ((HomeActivity) getActivity()).callInscriptionSortieAdherent(sortie.getIdSortie(), sortie.getIdAssociation());
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
}
