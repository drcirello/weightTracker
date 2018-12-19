package com.example.drcir.weighttracker;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface APIInterface {

    @FormUrlEncoded
    @POST("/rest-auth/login/")
    Call<AccountManagement> postLogin(@Field("username") String name, @Field("password") String pass);

}