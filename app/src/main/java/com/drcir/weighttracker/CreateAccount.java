package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final EditText userEmail = findViewById(R.id.create_account_useremail);
        final EditText userPass = findViewById(R.id.create_account_userpass);
        final Button createAccount = findViewById(R.id.create_account_button);

        userEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    userPass.requestFocus();
                }
                return false;
            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0 || userPass.getText().toString().trim().length()== 0){
                    createAccount.setEnabled(false);
                } else {
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        userPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(userPass.getText().toString().trim().length()!= 0 && userEmail.getText().toString().trim().length()!=0)
                        createAccount.performClick();
                }
                return false;
            }
        });

        userPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0 || userEmail.getText().toString().trim().length()== 0){
                    createAccount.setEnabled(false);
                } else {
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ImageView back = findViewById(R.id.create_account_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                CreateAccount.this.startActivity(intent);
                finish();
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String password = userPass.getText().toString();


                if(Utils.isEmailValid(email)) {
                    final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    if (Utils.checkConnection(CreateAccount.this, getString(R.string.no_connection_message_create_account))) {
                        Call<JsonObject> call = apiInterface.createUser(email, email, password, password);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    login(apiInterface, email, password);
                                    finish();
                                } else {
                                    try {
                                        String message;
                                        JSONObject errorMessage = new JSONObject(response.errorBody().string());
                                        if(errorMessage.has("email"))
                                            message = errorMessage.getJSONArray("email").getString(0);
                                        else if(errorMessage.has("password1"))
                                            message = errorMessage.getJSONArray("password1").getString(0);
                                        else
                                            message = getString(R.string.account_created_failed);
                                        createFailed(message);
                                    } catch (Exception e) {
                                        createFailed(getString(R.string.account_created_failed));
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                createFailed(getString(R.string.account_created_failed));
                                call.cancel();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void login(APIInterface apiInterface, String username, String userpass){
            Call<AccountManagement> call = apiInterface.postLogin(username, userpass);
            call.enqueue(new Callback<AccountManagement>() {
                @Override
                public void onResponse(Call<AccountManagement> call, Response<AccountManagement> response) {
                    response.body();
                    try {
                        String token = response.body().getToken();
                        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putString(getResources().getString(R.string.token_preference), token).apply();
                        mEditor.putString(getResources().getString(R.string.token_JWT_preference), "JWT " + token).apply();
                        mEditor.putLong(getResources().getString(R.string.token_date_preference), System.currentTimeMillis()).apply();
                        Intent intent = new Intent(CreateAccount.this, Base_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        CreateAccount.this.startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.creation_login_failed), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountManagement> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.creation_login_failed), Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        }

    public void createFailed(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
