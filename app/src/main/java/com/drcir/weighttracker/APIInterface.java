package com.drcir.weighttracker;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<AccountManagement> postLogin(@Field("username") String email, @Field("password") String pass);

    @POST("/rest-auth/logout/")
    Call<JsonObject> postLogout(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("rest-auth/registration/")
    Call<JsonObject> createUser(@Field("username") String username, @Field("email") String email, @Field("password1") String password1, @Field("password2") String password2);

    @FormUrlEncoded
    @POST("rest-auth/password/reset/")
    Call<JsonObject> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/tracker/weight/data/create/")
    Call<Void> createWeight(@Header("Authorization") String token, @Field("date") String date, @Field("weight") int weight);

    @POST("/tracker/weight/data/delete/{weight_id}")
    Call<Void> deleteWeight(@Header("Authorization") String token, @Path("weight_id") int weight_id);

    @FormUrlEncoded
    @POST("/rest-auth/refresh-token/")
    Call<AccountManagement> postRefreshToken(@Field("token") String token);

    @FormUrlEncoded
    @POST("/rest-auth/verify-token/")
    Call<JsonObject> postVerify(@Field("token") String token);

    @POST("/tracker/weight/data/user/")
    Call<List<WeightEntry>> getWeightData(@Header("Authorization") String token);

    @POST("/tracker/weight/data/entries/")
    Call<List<WeightEntry>> getWeightEntries(@Header("Authorization") String token);

}