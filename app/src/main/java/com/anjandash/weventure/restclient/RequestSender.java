package com.anjandash.weventure.restclient;

import android.content.Context;
import android.widget.Toast;

import com.anjandash.weventure.MainActivity;
import com.anjandash.weventure.restclient.model.Challenge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequestSender {
    private static WebApi INSTANCE = null;

    private static final String BASE_URL = "https://picventure.herokuapp.com";

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

    public interface CheckLocationCallBack {
        void onHistoricalPictureFound(Challenge challenge);
    }

    public static void sendLocation(final Context context, double lng, double lat, final CheckLocationCallBack checkLocationCallBack) {
        WebApi webApi = RequestSender.getWebAPI(context);
        Call<Challenge> call = webApi.checkLocation(lng, lat);
        call.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                if (response.isSuccessful()) {
                    checkLocationCallBack.onHistoricalPictureFound(response.body());
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
