package com.example.projetassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.Entities.Sorties;
import com.example.projetassociation.Fragments.HomeFragment;
import com.example.projetassociation.Fragments.SortiesFragment;
import com.example.projetassociation.Utilities.Functions;
import com.example.projetassociation.Utilities.ServiceWeb;
import com.example.projetassociation.Utilities.Session;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    Context context;
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments;
    SortiesFragment sortiesFragment;
    HomeFragment homeFragment;
    int index;
    Adherent adherent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;

        adherent = Session.getAdherent();

        //Functions.getToast(this, adherent.getNom() + " : " + Session.getId());

        if (adherent == null) {
            finish();
        }

        // Initialise la gestion de la toolbar
        initToolbar();

        // Initialise la gestion des fragments
        initFragments();

        // Fragment de démarrage
        index = 0;
        replaceFragment(homeFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeFragment.setAdherent(adherent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // On associe un menu à notre activity
        getMenuInflater().inflate(R.menu.mnu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;

        switch (id) {
            case R.id.action_quitter:
                finish();
                break;
            case R.id.action_compte:
                index = 0;
                homeFragment.setAdherent(adherent);
                break;
            case R.id.action_sorties:
                index = 1;
                callGetSorties(adherent.getIdAssociation());
                break;
        }

        fragment = fragments.get(index);
        replaceFragment(fragment);
        return true;
    }

    private void initFragments() {
        // On instancie nos fragments
        sortiesFragment = new SortiesFragment();
        homeFragment = new HomeFragment();

        // On ajoute nos fragments dans une list de fragments
        fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(sortiesFragment);

        fragmentManager = getFragmentManager();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        // on remplace l'actionbar par notre toolbar
        setSupportActionBar(toolbar);

        // on supprime le titre de l'application
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // j'ajoute un logo(drawable) dans la toolbar
        toolbar.setLogo(R.drawable.ic_action_asso);

        // je definis le titre de la toolbar
        toolbar.setTitle("AFPA Asso");

        toolbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        // on démarre une transaction pour gérer les differents fragments
        fragmentManager.beginTransaction()
                .replace(R.id.frm_home, fragment)
                .commit();
    }

    private void callGetSorties(int idAssociation) {
        ServiceWeb.callGetSorties(idAssociation, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String retourServiceWeb = response.body().string();

                 runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful()) {
                            Functions.getToast(context, "Erreur service web (code " + response.code() + ")");
                            return;
                        }

                        try {
                            Gson gson = new Gson();

                            //Matching du flux json qui vient du service web en objet Sorties
                            Sorties sorties = gson.fromJson(retourServiceWeb,Sorties.class);
                            sortiesFragment.loadSorties(sorties, context);

                        } catch (Exception ex) {
                            Log.e("Erreur callGetSorties", ex.getMessage());
                        }
                    }
                });
            }
        });
    }
}
