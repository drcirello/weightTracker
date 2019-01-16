package com.drcir.weighttracker;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface APIInterface {

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<AccountManagement> postLogin(@Field("username") String name, @Field("password") String pass);

    @POST("/rest-auth/logout/")
    Call<JsonObject> postLogout(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/tracker/weight/create/")
    Call<JsonObject> createWeight(@Header("Authorization") String token, @Field("active") Boolean active, @Field("player") int player, @Field("date") long date, @Field("weight") float weight);

    @FormUrlEncoded
    @POST("/tracker/weight/data/delete/")
    Call<JsonObject> deleteWeight(@Header("Authorization") String token, @Field("weight_id") int weight_id);

    @FormUrlEncoded
    @POST("/rest-auth/refresh-token/")
    Call<AccountManagement> postRefreshToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("/rest-auth/verify-token/")
    Call<JsonObject> postLogin(@Field("token") String token);

    @POST("/tracker/weight/data/user/")
    Call<List<WeightEntry>> getWeightData(@Header("Authorization") String token);

    @POST("/tracker/weight/data/entries/")
    Call<List<WeightEntry>> getWeightEntries(@Header("Authorization") String token);

}