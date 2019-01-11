package com.drcir.weighttracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    //converts to Month DD, YYYY format
    public static String formatDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(date);
    }



    public static void getWeightEntries(APIInterface apiInterface, String token){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<WeightEntry> call = apiInterface.getWeightData(token);
        call.enqueue(new Callback<WeightEntry>() {
            @Override
            public void onResponse(Call<WeightEntry> call, Response<WeightEntry> response) {
                if(response.isSuccessful()) {
                    response.body();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<WeightEntry>>(){}.getType();
                    //List<WeightEntry> we = gson.fromJson(response.body(), listType);
                    //dataset = we;
                }
            }

            @Override
            public void onFailure(Call<WeightEntry> call, Throwable t) {
                call.cancel();
            }
        });
    };

    /*public void refreshToken(String token){
        APIInterface apiInterface;
        Call<AccountManagement> call = apiInterface.postRefreshToken(token);
        call.enqueue(new Callback<AccountManagement>() {
            @Override
            public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                response.body();
                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor..putString(getResources().getString(R.string.token), token).apply();
                    mEditor.putLong(getResources().getString(R.string.token_date), System.currentTimeMillis()).apply();
                }
            }

            @Override
            public void onFailure(Call<AccountManagement> call, Throwable t) {
                call.cancel();
            }
        });
    };*/
}
