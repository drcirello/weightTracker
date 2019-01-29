package com.drcir.weighttracker;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Base_Activity extends AppCompatActivity implements FragmentSwapListener {
    FragmentManager fragmentManager;
    Toolbar myTitlebar;
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new ChartsFragment());
        fragmentTransaction.commit();

        myTitlebar = findViewById(R.id.titleBar);
        myTitlebar.setTitle(getString(R.string.charts_title));
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_charts);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        swapFragment(item.getItemId());
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        Long tokenTime = mSharedPreferences.getLong(getString(R.string.token_date_preference), 0);
        if(System.currentTimeMillis() - tokenTime > TimeConversions.TOKEN_REFRESH_TIME){
            Intent intent = new Intent(Base_Activity.this, Main.class);
            Utils.refreshToken(mSharedPreferences, Base_Activity.this, intent);
        }
    }

    @Override
    public void swapFragment(int id){
        Fragment fragment = null;
        String title;
        Class fragmentClass;
        switch (id) {
            case R.id.action_charts:
                fragmentClass = ChartsFragment.class;
                title = getString(R.string.charts_title);
                break;
            case R.id.action_create:
                fragmentClass = EnterWeightFragment.class;
                title = getString(R.string.enter_weight_title);
                break;
            case R.id.action_view:
                fragmentClass = WeightEntriesFragment.class;
                title = getString(R.string.entries_title);
                break;
            case R.id.action_settings:
                fragmentClass = SettingsFragment.class;
                title = getString(R.string.settings_title);
                break;
            default:
                fragmentClass = ChartsFragment.class;
                title = getString(R.string.charts_title);
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        navBar.getMenu().findItem(id).setChecked(true);
        myTitlebar.setTitle(title);
    }
}