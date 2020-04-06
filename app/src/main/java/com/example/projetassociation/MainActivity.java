package com.example.projetassociation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.Utilities.Functions;
import com.example.projetassociation.Utilities.ServiceWeb;
import com.example.projetassociation.Utilities.Session;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    Context context;
    String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // On récupère le sessionId qui vient de mon SplashScreen
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("sessionId");

        if (sessionId.isEmpty()) {
            finish();
        }

        final EditText txtLogin = findViewById(R.id.txtLogin);
        final EditText txtPassword = findViewById(R.id.txtPassword);

        Button btnConnexion = findViewById(R.id.btnConnexion);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = txtLogin.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (login.isEmpty() || password.isEmpty()) {
                    Functions.getToast(context, "Veuillez saisir vos identifiants");
                    return;
                }

                // Appel asynchrone du service web ()
                ServiceWeb.callLoginAdherent(login, password, new Callback() {
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
                                if (!response.isSuccessful()) {
                                    Functions.getToast(context, "Erreur service web (code " + response.code() + ")");
                                    return;
                                }

                                if (retourServiceWeb.equals("\"\"")) {
                                    Functions.getToast(context, "login ou mot de passe incorrect");
                                    return;
                                }

                                try {
                                    Gson gson = new Gson();
                                    Adherent adherent = gson.fromJson(retourServiceWeb, Adherent.class);

                                    // Ajoute un adherent à ma session
                                    Session.setAdherent(adherent);
                                    Session.setId(sessionId);

                                    // redirige vers notre activity principale
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception ex) {
                                    Log.e("Erreur MainActivity", ex.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
