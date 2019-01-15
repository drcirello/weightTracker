package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    //converts to Month DD, YYYY format
    public static String formatDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String formatSelectedDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
        return sdf.format(date);
    }

    public static DecimalFormat getDecimalFormat(){
        return new DecimalFormat("0.0");
    }

    public static void logout(SharedPreferences sharedPrefToken, final Context context, final Intent intent){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        String token = sharedPrefToken.getString(context.getString(R.string.token_JWT_preference), null);
        SharedPreferences.Editor mEditorToken = sharedPrefToken.edit();
        mEditorToken.clear();
        mEditorToken.commit();


        Call<JsonObject> call = apiInterface.postLogout(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                context.startActivity(intent);
            }
        });
    };

}
