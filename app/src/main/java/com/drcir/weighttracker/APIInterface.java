package com.drcir.weighttracker;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface APIInterface {

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<AccountManagement> postLogin(@Field("username") String name, @Field("password") String pass);

    @FormUrlEncoded
    @POST("/rest-auth/refresh-token/")
    Call<AccountManagement> postRefreshToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("/rest-auth/verify-token/")
    Call<JsonObject> postLogin(@Field("token") String token);

    @FormUrlEncoded
    @POST("/tracker/weight/data/rest/")
    Call<WeightEntry> getWeightData(@Field("token") String token);

}