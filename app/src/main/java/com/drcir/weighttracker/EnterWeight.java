package com.drcir.weighttracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;


public class EnterWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_weight);
        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_create);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        Intent intent;
                        switch (id) {
                            case R.id.action_charts:
                                intent = new Intent(EnterWeight.this, Charts.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_view:
                                intent = new Intent(EnterWeight.this, WeightEntries.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(EnterWeight.this, Settings.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_create:
                            default:
                                break;
                        }
                        return false;
                    }
                });
    }
}
