package com.drcir.weighttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText useremail = findViewById(R.id.reset_password_useremail);

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
                //Generate and send Password
                Toast.makeText(ResetPassword.this, "Password Reset!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPassword.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ResetPassword.this.startActivity(intent);
                finish();
            }
        });
    }
}