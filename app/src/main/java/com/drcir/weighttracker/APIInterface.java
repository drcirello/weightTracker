package com.drcir.weighttracker;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface APIInterface {

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<AccountManagement> postLogin(@Field("username") String name, @Field("password") String pass);

    @POST("/rest-auth/logout/")
    Call<JsonObject> postLogout(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/rest-auth/refresh-token/")
    Call<AccountManagement> postRefreshToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("/rest-auth/verify-token/")
    Call<JsonObject> postLogin(@Field("token") String token);

    @POST("/tracker/weight/data/rest/")
    Call<WeightEntry> getWeightData(@Header("Authorization") String token);

}