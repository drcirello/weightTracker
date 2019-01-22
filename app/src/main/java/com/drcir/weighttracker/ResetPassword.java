package com.drcir.weighttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText useremail = findViewById(R.id.reset_password_useremail);

        ImageView back = findViewById(R.id.reset_password_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ResetPassword.this.startActivity(intent);
                finish();
            }
        });

        Button sendPassword = findViewById(R.id.send_password);
        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = useremail.getText().toString();

                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                if(Utils.checkConnection(ResetPassword.this, getString(R.string.no_connection_message_reset))) {
                    Call<JsonObject> call = apiInterface.resetPassword(email);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(ResetPassword.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ResetPassword.this.startActivity(intent);
                                Toast.makeText(ResetPassword.this, "Password Reset Sent", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                resetFailed();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            resetFailed();
                            call.cancel();
                        }
                    });
                }
            }
        });

    }

    public void resetFailed(){
        Toast.makeText(getApplicationContext(), "Password Reset Failed", Toast.LENGTH_LONG).show();
    }
}