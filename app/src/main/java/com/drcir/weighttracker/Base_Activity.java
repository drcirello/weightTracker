package com.drcir.weighttracker;

import android.graphics.drawable.Drawable;
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

    SharedPreferences mSharedPrefToken;
    SharedPreferences mSharedPrefRange;

    APIInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDataSetChartsChanged = true;
        mDataSetEntriesChanged = true;
        mNoDataImage = null;

        mSharedPrefToken = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        mSharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
        mApiInterface = APIClient.getClient().create(APIInterface.class);

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
                        swapFragment(item.getItemId(), true);
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mDataSetChartsChanged = true;
        mDataSetEntriesChanged = true;
        
        Long tokenTime = mSharedPrefToken.getLong(getString(R.string.token_date_preference), 0);
        if(System.currentTimeMillis() - tokenTime > TimeConversions.TOKEN_REFRESH_TIME){
            Intent intent = new Intent(Base_Activity.this, Main.class);
            Utils.refreshToken(mSharedPrefToken, Base_Activity.this, intent);
        }
    }

    @Override
    public void onBackPressed(){
        if(fragmentManager.getBackStackEntryCount() >= 1) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            setNavigation(Integer.parseInt(fragmentTag));
        }
        super.onBackPressed();
    }

    @Override
    public void swapFragment(int id, boolean addToStack){
        int currentId = navBar.getSelectedItemId();
        if(id != currentId) {
            Fragment fragment = null;
            Class fragmentClass;
            switch (id) {
                case R.id.action_charts:
                    fragmentClass = ChartsFragment.class;
                    break;
                case R.id.action_create:
                    fragmentClass = EnterWeightFragment.class;
                    break;
                case R.id.action_view:
                    fragmentClass = WeightEntriesFragment.class;
                    break;
                case R.id.action_settings:
                    fragmentClass = SettingsFragment.class;
                    break;
                default:
                    fragmentClass = ChartsFragment.class;
                    break;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment);
            if (addToStack)
                transaction.addToBackStack(Integer.toString(currentId));
            transaction.commit();

            setNavigation(id);
        }
    }

    public void setNavigation(int id){
        String title;
        switch (id) {
            case R.id.action_charts:
                title = getString(R.string.charts_title);
                break;
            case R.id.action_create:
                title = getString(R.string.enter_weight_title);
                break;
            case R.id.action_view:
                title = getString(R.string.entries_title);
                break;
            case R.id.action_settings:
                title = getString(R.string.settings_title);
                break;
            default:
                title = getString(R.string.charts_title);
                break;
        }
        navBar.getMenu().findItem(id).setChecked(true);
        myTitlebar.setTitle(title);
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
    public SharedPreferences getTokenPref(){
        return mSharedPrefToken;
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
}