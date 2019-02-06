package com.drcir.weighttracker;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @FormUrlEncoded
    @POST("rest/create/")
    Call<Void> createUserFirebase(@Field("token") String token);

    @FormUrlEncoded
    @POST("/tracker/weight/data/create/")
    Call<Void> createWeight(@Header("Authorization") String token, @Field("date") String date, @Field("weight") int weight);

    @POST("/tracker/weight/data/delete/{weight_id}")
    Call<Void> deleteWeight(@Header("Authorization") String token, @Path("weight_id") int weight_id);

    @POST("/tracker/weight/data/user/")
    Call<List<WeightEntry>> getWeightData(@Header("Authorization") String token);

    @POST("/tracker/weight/data/entries/")
    Call<List<WeightEntry>> getWeightEntries(@Header("Authorization") String token);
}