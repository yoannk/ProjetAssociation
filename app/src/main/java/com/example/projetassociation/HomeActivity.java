package com.example.projetassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.Utilities.Functions;
import com.example.projetassociation.Utilities.Session;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;

        Adherent adherent = Session.getAdherent();

        Functions.getToast(this, adherent.getNom() + " : " + Session.getId());

        if (adherent == null) {
            finish();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // On associe un menu à notre activity
        getMenuInflater().inflate(R.menu.mnu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_quitter:
                finish();
                break;
        }

        return true;
    }
}
