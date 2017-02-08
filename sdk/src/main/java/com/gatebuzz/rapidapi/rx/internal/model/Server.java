package com.gatebuzz.rapidapi.rx.internal.model;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class Server {
    public final Gson gson;
    public final OkHttpClient okHttpClient;
    public final String serverUrl;

    public Server(String serverUrl, OkHttpClient okHttpClient, Gson gson) {
        this.okHttpClient = okHttpClient;
        this.serverUrl = serverUrl;
        this.gson = gson;
    }
}
