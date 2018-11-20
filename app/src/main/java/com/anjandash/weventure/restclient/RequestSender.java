package com.anjandash.weventure.restclient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.anjandash.weventure.restclient.model.Challenge;
import com.anjandash.weventure.restclient.model.ChallengeResult;
import com.anjandash.weventure.restclient.model.NewChallenge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequestSender {
    private static final String TAG = "RequestSender";
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
        void onHistoricalPictureFound(Context context, Challenge challenge);
    }

    public static void sendLocation(final Context context, double lng, double lat, final CheckLocationCallBack checkLocationCallBack) {
        WebApi webApi = RequestSender.getWebAPI(context);
        Call<Challenge> call = webApi.checkLocation(lng, lat);
        call.enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                if (response.isSuccessful()) {
                    Challenge challenge = response.body();
                    if (challenge.getFound() == 1) {
                        checkLocationCallBack.onHistoricalPictureFound(context, response.body());
                    } else {
                        Log.i(TAG, "No historical photos nearby.");
                    }
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

    public interface SubmitChallengePhotoCallback {
        public void onChallengeResult(ChallengeResult challengeResult);
    }

    public static void submitChallengePhoto(final Context context,
                                            NewChallenge newChallenge,
                                            final SubmitChallengePhotoCallback submitChallengePhotoCallback) {
        WebApi webApi = RequestSender.getWebAPI(context);
        Call<ChallengeResult> call = webApi.submitChallengePhoto(newChallenge);
        call.enqueue(new Callback<ChallengeResult>() {
            @Override
            public void onResponse(Call<ChallengeResult> call, Response<ChallengeResult> response) {
                if (response.isSuccessful()) {
                    submitChallengePhotoCallback.onChallengeResult(response.body());
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ChallengeResult> call, Throwable t) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
