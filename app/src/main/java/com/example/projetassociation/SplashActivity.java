package com.example.projetassociation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.projetassociation.Utilities.Constants;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SleepTask sleepTask = new SleepTask();
        sleepTask.execute();


    }

    private class SleepTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            /*
            // simule un appel service web
            try {
                Thread.sleep(7000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return callServiceWeb("getid");
        }

        @Override
        protected void onPostExecute(String sessionId) {
            super.onPostExecute(sessionId);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("sessionId", sessionId);
            startActivity(intent);
            finish();
        }
    }

    private String callServiceWeb(String serviceWeb) {
        String url = Constants.urlSW + serviceWeb; // login:tt password:tt
        OkHttpClient client = new OkHttpClient();
        String result = "";

        Request request = new Request.Builder().url(url).build();

        try {

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
}
