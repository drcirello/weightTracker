package com.example.drcir.weighttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

        TextView responseText;
        APIInterface apiInterface;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            responseText = findViewById(R.id.test);

            EditText useremail = findViewById(R.id.login_useremail);
            EditText userpass = findViewById(R.id.login_userpass);
            
            Button back = findViewById(R.id.login_back);
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
                    login("dave", "password");

                    apiInterface = APIClient.getClient().create(APIInterface.class);

                    Call<MultipleResource> call = apiInterface.getStatus("dave", "dc1268cd");
                    call.enqueue(new Callback<MultipleResource>() {
                        @Override
                        public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {


                            Log.d("TAG", response.code() + "");

                            MultipleResource resource = response.body();
                            Integer text = resource.status;
                            responseText.setText("Working");

                        }

                        @Override
                        public void onFailure(Call<MultipleResource> call, Throwable t) {
                            call.cancel();
                        }
                    });

                    //Toast.makeText(Login.this, "LOGIN!", Toast.LENGTH_SHORT).show();
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

        };
}
