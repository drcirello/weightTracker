package com.example.drcir.weighttracker;

import com.example.drcir.weighttracker.MultipleResource;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface APIInterface {

    @FormUrlEncoded
    @POST("/accounts/login")
    Call<MultipleResource> getStatus(@Field("username") String name, @Field("password") String pass);

}