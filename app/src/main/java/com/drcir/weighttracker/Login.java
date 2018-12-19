package com.example.drcir.weighttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
            Call<AccountManagement> call = apiInterface.postLogin(username, userpass);
            call.enqueue(new Callback<AccountManagement>() {
                @Override
                public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {

                    response.body();
                    if(response.body() != null) {
                        token = response.body().getToken();
                        user = response.body().getUser();
                        //STORE TOKEN
                    }
                    //IF VALID RESPONSE
                    if(token != null) {
                        Intent i = new Intent(Login.this, EnterWeight.class);
                        Login.this.startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountManagement> call, Throwable t) {
                    call.cancel();
                }
            });
        };
}
