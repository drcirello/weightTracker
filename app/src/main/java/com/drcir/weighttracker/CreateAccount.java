package com.drcir.weighttracker;

import android.content.Intent;
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
                String email = userEmail.getText().toString();
                String password = userPass.getText().toString();


                if(Utils.isEmailValid(email)) {
                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    if (Utils.checkConnection(CreateAccount.this, getString(R.string.no_connection_message_create_account))) {
                        Call<JsonObject> call = apiInterface.createUser(email, email, password, password);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    Intent intent = new Intent(CreateAccount.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    CreateAccount.this.startActivity(intent);
                                    Toast.makeText(CreateAccount.this, getString(R.string.account_created), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    createFailed();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                createFailed();
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

    public void createFailed(){
        Toast.makeText(getApplicationContext(), getString(R.string.account_created_failed), Toast.LENGTH_LONG).show();
    }
}
