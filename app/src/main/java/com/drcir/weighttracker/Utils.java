package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String formatSelectedDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
        return sdf.format(date);
    }

    public static DecimalFormat getDecimalFormat(){
        return new DecimalFormat("0");
    }

    public static boolean checkConnection(Context context, String failedMessage) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            connected = true;
        if(!connected) {
            Toast.makeText(context, failedMessage, Toast.LENGTH_LONG).show();
            Log.i("CONNECTION CHECK: ", "No connection");
        }
        return connected;
    }

    public static void refreshToken(final SharedPreferences sharedPrefToken, final Context context, final Intent intent){

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        String token = sharedPrefToken.getString(context.getString(R.string.token_preference), null);

        Call<AccountManagement> call = apiInterface.postRefreshToken(token);
        call.enqueue(new Callback<AccountManagement>() {
            @Override
            public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                response.body();
                if(response.isSuccessful()) {
                    String token = response.body().getToken();
                    SharedPreferences.Editor mEditor = sharedPrefToken.edit();
                    mEditor.putString(context.getResources().getString(R.string.token_preference), token).apply();
                    mEditor.putString(context.getResources().getString(R.string.token_JWT_preference), "JWT " + token).apply();
                    mEditor.putLong(context.getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                }
                else{
                    Toast.makeText(context, context.getString(R.string.login_expired), Toast.LENGTH_LONG).show();
                    logout(sharedPrefToken, context, intent);
                }
            }

            @Override
            public void onFailure(Call<AccountManagement> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.login_expired), Toast.LENGTH_LONG).show();
                logout(sharedPrefToken, context, intent);
            }
        });
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
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
