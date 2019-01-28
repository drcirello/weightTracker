package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drcir.weighttracker.data.DataDefinitions;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    APIInterface apiInterface;
    String token;
    Long tokenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        token = mSharedPreferences.getString(getString(R.string.token_preference), null);
        tokenDate = mSharedPreferences.getLong(getString(R.string.token_date_preference), System.currentTimeMillis());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        if(token != null && Utils.checkConnection(this, getString(R.string.no_connection_message))){
            validateToken();
        }
        else{
            noToken();
        }

    }

    public void validateToken(){
        Call<JsonObject> call = apiInterface.postLogin(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.code() == 200) {
                    doLogin();
                }
                else{
                    noToken();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                noToken();
            }
        });
    }

    public void noToken(){
        setContentView(R.layout.activity_main);
        Button createAcount = findViewById(R.id.createAccount);
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditorToken = mSharedPreferences.edit();
        mEditorToken.clear();
        mEditorToken.commit();
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

    public void doLogin(){
        if((System.currentTimeMillis() - tokenDate) > DataDefinitions.ONE_WEEK){
            refreshToken();
        }
        Intent i = new Intent(Main.this, Charts.class);
        Main.this.startActivity(i);
        finish();
    }

    public void refreshToken(){
        Call<AccountManagement> call = apiInterface.postRefreshToken(token);
        call.enqueue(new Callback<AccountManagement>() {
            @Override
            public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                try{
                    token = response.body().getToken();
                    SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putString(getResources().getString(R.string.token_preference), token).apply();
                    mEditor.putString(getResources().getString(R.string.token_JWT_preference), "JWT " + token).apply();
                    mEditor.putLong(getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                }
                catch (Exception e){}
            }

            @Override
            public void onFailure(Call<AccountManagement> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
