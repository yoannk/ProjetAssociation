package com.example.projetassociation.Utilities;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServiceWeb {

    private static OkHttpClient client = new OkHttpClient();

    public static void callGetSorties(int idAssociation, Callback callback) {
        String url = Constants.urlSW + "GetSorties";

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        httpBuilder.addQueryParameter("idAssociation", "" + idAssociation);

        Request request = new Request.Builder().url(httpBuilder.build()).build();

        client.newCall(request).enqueue(callback);
    }

    public static void callLoginAdherent(String login, String password, String idSession, Callback callback) {
        String url = Constants.urlSW + "LoginAdherent";

        // POST request
        RequestBody formBody = new FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .add("idsession", idSession)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void callGetSessionId(Callback callback) {
        String url = Constants.urlSW + "GetId";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(callback);
    }

    public static void callUpdateAdherent(String idSession, String email, String telephone, String password, String solde, Callback callback) {
        String url = Constants.urlSW + "UpdateAdherent";

        // POST request
        RequestBody formBody = new FormBody.Builder()
                .add("idsession", idSession)
                .add("email", email)
                .add("telephone", telephone)
                .add("password", password)
                .add("solde", solde)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
