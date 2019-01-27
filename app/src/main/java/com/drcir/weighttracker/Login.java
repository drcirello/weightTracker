package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
            final Button login = findViewById(R.id.login_button);

            useremail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if(userpass.getText().toString().trim().length()!= 0 && useremail.getText().toString().trim().length()!=0)
                            login.performClick();
                    }
                    return false;
                }
            });

            useremail.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.toString().trim().length()==0 || userpass.getText().toString().trim().length()== 0){
                        login.setEnabled(false);
                    } else {
                        login.setEnabled(true);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}
            });

            userpass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        Toast.makeText(Login.this, "Move to create", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });

            userpass.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(s.toString().trim().length()==0 || useremail.getText().toString().trim().length()== 0){
                        login.setEnabled(false);
                    } else {
                        login.setEnabled(true);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}
            });

            ImageView back = findViewById(R.id.login_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Login.this.startActivity(intent);
                    finish();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    login(useremail.getText().toString(), userpass.getText().toString());
                }
            });

        }

        public void login(String username, String userpass){

            //TODO testing
            //username = "dave";
            //userpass = "password";
            username = "weighttrackertest@gmail.com";
            userpass = "Weighttest123$";
            //username = "weightTrackerTest2@gmail.com";
            //userpass = "Weighttest123$";
            if(Utils.checkConnection(Login.this, getString(R.string.no_connection_message_login))) {
                Call<AccountManagement> call = apiInterface.postLogin(username, userpass);
                call.enqueue(new Callback<AccountManagement>() {
                    @Override
                    public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                        response.body();
                        try {
                            token = response.body().getToken();
                            user = response.body().getUser();
                            SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                            mEditor.putString(getResources().getString(R.string.token_preference), token).apply();
                            mEditor.putString(getResources().getString(R.string.token_JWT_preference), "JWT " + token).apply();
                            mEditor.putLong(getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                            Intent intent = new Intent(Login.this, Charts.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Login.this.startActivity(intent);
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
        };
}
