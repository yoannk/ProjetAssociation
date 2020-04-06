package com.example.projetassociation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.projetassociation.Utilities.ServiceWeb;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ServiceWeb.callGetSessionId(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String retourServiceWeb = response.body().string();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("sessionId", retourServiceWeb);
                startActivity(intent);
                finish();
            }
        });
    }
}
