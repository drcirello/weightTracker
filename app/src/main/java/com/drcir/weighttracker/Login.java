package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

        APIInterface apiInterface;
        String token = null;
        User user = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            final EditText useremail = findViewById(R.id.login_useremail);
            final EditText userpass = findViewById(R.id.login_userpass);

            ImageView back = findViewById(R.id.login_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Login.this, Main.class);
                    Login.this.startActivity(i);
                }
            });

            Button login = findViewById(R.id.login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    login(useremail.getText().toString(), userpass.getText().toString());
                }
            });

            LinearLayout resetPassword = findViewById(R.id.resetPassword);
            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                  Intent i = new Intent(Login.this, ResetPassword.class);
                  Login.this.startActivity(i);
                }
            });
        }

        public void login(String username, String userpass){

            //TODO testing
            username = "dave";
            userpass = "password";

            Call<AccountManagement> call = apiInterface.postLogin(username, userpass);
            call.enqueue(new Callback<AccountManagement>() {
                @Override
                public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                    response.body();
                    if(response.isSuccessful() && response.body() != null) {
                        token = "JWT " + response.body().getToken();
                        user = response.body().getUser();
                        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putString(getResources().getString(R.string.token_preference), token).apply();
                        mEditor.putLong(getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                        Intent i = new Intent(Login.this, Charts.class);
                        Login.this.startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountManagement> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        };
}
