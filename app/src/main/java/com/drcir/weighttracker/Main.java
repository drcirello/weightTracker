package com.drcir.weighttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sharedPref.getString(getString(R.string.token), null);
        Long tokenDate = sharedPref.getLong(getString(R.string.token_date), System.currentTimeMillis());

        Intent i = new Intent(Main.this, Login.class);
        Main.this.startActivity(i);

        /*
        if(token != null){
            apiInterface = APIClient.getClient().create(APIInterface.class);
            validateToken(token, tokenDate);
        }
        else {
            setContentView(R.layout.activity_main);
            Button createAcount = findViewById(R.id.createAccount);
            createAcount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Main.this, CreateAccount.class);
                    Main.this.startActivity(i);
                }
            });

            Button login = findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Main.this, Login.class);
                    Main.this.startActivity(i);
                }
            });
        }
        */

    }

    public void validateToken(final String token, final Long tokenDate){
        Call<JsonObject> call = apiInterface.postLogin(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    //10 days = 864000
                    if((System.currentTimeMillis() - tokenDate) > 864000){
                        refreshToken(token);
                    }
                    Intent i = new Intent(Main.this, EnterWeight.class);
                    Main.this.startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
            }
        });
    };

    public void refreshToken(String token){
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
    };
}
