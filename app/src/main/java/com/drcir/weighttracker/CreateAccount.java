package com.example.drcir.weighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText useremail = findViewById(R.id.create_account_useremail);
        EditText userpass = findViewById(R.id.create_account_userpass);

        ImageView back = findViewById(R.id.create_account_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccount.this, Main.class);
                CreateAccount.this.startActivity(i);
            }
        });

        Button login = findViewById(R.id.create_account_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attempt account creation
                Toast.makeText(CreateAccount.this, "Created!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
