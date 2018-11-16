package com.anjandash.weventure.restclient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequestSender {
    private static WebApi INSTANCE = null;

    private static final String BASE_URL = "https://picventure.herokuapp.com/api/v1/";

    public synchronized static WebApi getWebAPI(Context context) {
        if (null == INSTANCE) {
            INSTANCE = createWebAPI(context);
        }
        return INSTANCE;
    }

    private static WebApi createWebAPI(final Context context) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        WebApi webApi = retrofit.create(WebApi.class);
        return webApi;
    }
}
