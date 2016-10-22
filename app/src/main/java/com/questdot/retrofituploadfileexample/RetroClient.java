package com.questdot.retrofituploadfileexample;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroClient {

    private static final String ROOT_URL = "http://192.168.0.111/testupload/";


    public RetroClient() {

    }

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FileApi getApiService() {
        return getRetroClient().create(FileApi.class);
    }
}
