package com.anjandash.weventure.restclient;

import com.anjandash.weventure.restclient.model.Challenge;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hlib on 9/22/18.
 */

public interface WebApi {
    @GET("/api/v1.0/check-location/{lng}/{lat}")
    Call<Challenge> checkLocation(@Path("lng") double lng, @Path("lat") double lat);

}