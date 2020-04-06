package com.example.projetassociation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetassociation.Entities.Adherent;
import com.example.projetassociation.Utilities.Functions;
import com.example.projetassociation.Utilities.Session;
import com.google.gson.Gson;

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


        // http://www.claudehenry.fr/serviceweb/getassociations
        // http://www.claudehenry.fr/serviceweb/getid

        Button btnConnexion = findViewById(R.id.btnConnexion);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = txtLogin.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (!login.isEmpty() && !password.isEmpty()) {
                    // Appel du service web via une tache asynchrone ()
                    AsyncCallWS asyncCallWS = new AsyncCallWS(login, password);
                    asyncCallWS.execute();
                } else {
                    Functions.getToast(context, "Veuillez saisir vos identifiants");
                }
            }
        });
    }

    private class AsyncCallWS extends AsyncTask<String, Integer, String> {

        private final String login;
        private final String password;

        public AsyncCallWS(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return callServiceWeb(this.login, this.password);
        }

        @Override
        protected void onPostExecute(String retourServiceWeb) {
            super.onPostExecute(retourServiceWeb);
            //Functions.getToast(context, retourServiceWeb);

            if (!retourServiceWeb.isEmpty()) {
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
                    Log.e("Erreur service web", ex.getMessage());
                }
            }
        }
    }

    private String callServiceWeb(String login, String password) {
        String url = "http://www.claudehenry.fr/serviceweb/LoginAdherent"; // login:tt password:tt
        OkHttpClient client = new OkHttpClient();
        String result = "";

        //Request request = new Request.Builder().url(url).build();

        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
        httpBuider.addQueryParameter("login", login);
        httpBuider.addQueryParameter("password", password);


        try {
            Request request = new Request.Builder().url(httpBuider.build()).build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }

        } catch (Exception ex) {
            result = ex.getMessage();
            Log.e("Erreur service web", result);
        }

        return result;
    }

    /*public void run() throws Exception {
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });
    }*/
}
