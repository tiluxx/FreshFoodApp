package com.application.freshfoodapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UPCBarcodeAPIClient {
    private static UPCBarcodeAPIClient instance;
    private static Retrofit retrofit = null;
    private UPCBarcodeAPIClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.upcdatabase.org")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static UPCBarcodeAPIClient getInstance() {
        if(instance == null)
            instance = new UPCBarcodeAPIClient();
        return instance;
    }

    public static Retrofit getClient() {
        return retrofit;
    }
}