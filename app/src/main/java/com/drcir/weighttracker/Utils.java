package com.drcir.weighttracker;

import android.preference.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    /*public void refreshToken(String token){
        APIInterface apiInterface;
        Call<AccountManagement> call = apiInterface.postRefreshToken(token);
        call.enqueue(new Callback<AccountManagement>() {
            @Override
            public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                response.body();
                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(getResources().getString(R.string.token), token).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putLong(getResources().getString(R.string.token_date), System.currentTimeMillis()).apply();
                }
            }

            @Override
            public void onFailure(Call<AccountManagement> call, Throwable t) {
                call.cancel();
            }
        });
    };*/
}
