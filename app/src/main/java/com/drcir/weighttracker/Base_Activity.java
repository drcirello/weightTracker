package com.drcir.weighttracker;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.List;

public class Base_Activity extends AppCompatActivity implements BaseActivityListener {
    FragmentManager fragmentManager;
    Toolbar myTitlebar;
    BottomNavigationView navBar;
    List<WeightEntry> mDataSetCharts;
    List<WeightEntry> mDataSetEntries;
    boolean mDataSetChartsChanged;
    boolean mDataSetEntriesChanged;
    Drawable mNoDataImage;

    private FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;

    SharedPreferences mSharedPrefRange;

    APIInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDataSetChartsChanged = true;
        mDataSetEntriesChanged = true;
        mNoDataImage = null;


        mSharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
        mApiInterface = APIClient.getClient().create(APIInterface.class);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new EnterWeightFragment());
        fragmentTransaction.commit();

        myTitlebar = findViewById(R.id.titleBar);
        myTitlebar.setTitle(getString(R.string.enter_weight_title));
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_create);
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
        mSharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(Utils.checkConnection(this, getString(R.string.no_connection_message))) {
            mCurrentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (!task.isSuccessful()) {
                            Intent intent = new Intent(Base_Activity.this, Main.class);
                            Utils.logout(Base_Activity.this, intent);
                        }
                    }
                });
        }
    }

    @Override
    public void onBackPressed(){
        if(navBar.getSelectedItemId() != R.id.action_charts) {
            swapFragment(R.id.action_charts);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void swapFragment(int id){
        int currentId = navBar.getSelectedItemId();
        if(id != currentId) {
            Fragment fragment = null;
            Class fragmentClass;
            String title;
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

    @Override
    public List<WeightEntry> getDataSetCharts(){
        return mDataSetCharts;
    }

    @Override
    public void setDataSetCharts(List<WeightEntry> dataSet){
        mDataSetCharts = dataSet;
        mDataSetChartsChanged = false;
    }

    @Override
    public boolean getUpdateDataSetCharts(){
        return mDataSetChartsChanged;
    };

    @Override
    public List<WeightEntry> getDataSetEntries(){
        return mDataSetEntries;
    }

    @Override
    public void setDataSetEntries(List<WeightEntry> dataSet){
        mDataSetEntries = dataSet;
        mDataSetEntriesChanged = false;
    }

    @Override
    public boolean getUpdateDataSetEntries(){
        return mDataSetEntriesChanged;
    }

    @Override
    public void setUpdateDataSets(boolean update){
        mDataSetChartsChanged = update;
        mDataSetEntriesChanged = update;
    }

    @Override
    public SharedPreferences getRangePref() {
        return mSharedPrefRange;
    }

    @Override
    public APIInterface getApiInterface() {
        return mApiInterface;
    }

    @Override
    public Drawable getNoDataImage(){
        return mNoDataImage;
    }

    @Override
    public void setNoDataImage(Drawable image){
        mNoDataImage = image;
    }

    @Override
    public FirebaseUser getCurrentUser(){
        return mCurrentUser;
    }
}