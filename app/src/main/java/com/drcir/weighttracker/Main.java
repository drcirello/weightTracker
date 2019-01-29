package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.drcir.weighttracker.data.DataDefinitions;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity implements AccountManagementListener {

    FragmentManager fragmentManager;
    APIInterface apiInterface;
    String token;
    Long tokenDate;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        token = mSharedPreferences.getString(getString(R.string.token_preference), null);
        tokenDate = mSharedPreferences.getLong(getString(R.string.token_date_preference), System.currentTimeMillis());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if(token != null && Utils.checkConnection(this, getString(R.string.no_connection_message))){
            validateToken();
        }
        else{
            loginRequired();
        }
    }

    public void loginRequired(){
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, new AccountFragment());
        fragmentTransaction.commit();
        SharedPreferences.Editor mEditorToken = mSharedPreferences.edit();
        mEditorToken.clear();
        mEditorToken.commit();
    }

    public void validateToken(){
        Call<JsonObject> call = apiInterface.postVerify(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.code() == 200) {
                    performLogin();
                }
                else{
                    loginRequired();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                loginRequired();
            }
        });
    }

    public void performLogin(){
        if((System.currentTimeMillis() - tokenDate) > DataDefinitions.ONE_WEEK){
            refreshToken();
        }
        Intent i = new Intent(Main.this, Base_Activity.class);
        Main.this.startActivity(i);
        finish();
    }

    public void requestLogin(String username, String userpass){
        if(Utils.checkConnection(Main.this, getString(R.string.no_connection_message_login))) {
            Call<AccountManagement> call = apiInterface.postLogin(username, userpass);
            call.enqueue(new Callback<AccountManagement>() {
                @Override
                public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                    response.body();
                    try {
                        token = response.body().getToken();
                        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putString(getResources().getString(R.string.token_preference), token).apply();
                        mEditor.putString(getResources().getString(R.string.token_JWT_preference), "JWT " + token).apply();
                        mEditor.putLong(getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                        Intent intent = new Intent(Main.this, Base_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Main.this.startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountManagement> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        }
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

    public void swapFragment(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.fragment_container_main, fragment).commit();
    }

    public void login(String username, String userpass){
        requestLogin(username, userpass);
    }
}
