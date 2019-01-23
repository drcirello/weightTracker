package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.drcir.weighttracker.data.DataDefinitions;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myTitlebar = findViewById(R.id.titleBar);
        setSupportActionBar(myTitlebar);
        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_settings);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();

                        Intent intent;
                        switch (id) {
                            case R.id.action_view:
                                intent = new Intent(Settings.this, WeightEntries.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_create:
                                intent = new Intent(Settings.this, EnterWeight.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_charts:
                                intent = new Intent(Settings.this, Charts.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                            default:
                                break;
                        }
                        return false;
                    }
                });

        final SparseIntArray ranges = new SparseIntArray();
        ranges.append(DataDefinitions.ONE_WEEK, 0);
        ranges.append(DataDefinitions.ONE_MONTH, 1);
        ranges.append(DataDefinitions.THREE_MONTHS, 2);
        ranges.append(DataDefinitions.SIX_MONTHS, 3);
        ranges.append(DataDefinitions.ONE_YEAR, 4);
        ranges.append(DataDefinitions.YTD, 5);
        ranges.append(DataDefinitions.MAX, 6);

        SharedPreferences sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
        int defaultChartRange = sharedPrefRange.getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX);
        Spinner spinnerChartRange = (Spinner) findViewById(R.id.selectedRange);
        ArrayAdapter<CharSequence> adapterRange = ArrayAdapter.createFromResource(this,
                R.array.chart_ranges, android.R.layout.simple_spinner_item);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChartRange.setAdapter(adapterRange);
        spinnerChartRange.setSelection(ranges.get(defaultChartRange));
        spinnerChartRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_range_preference), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int defaultOverTime1 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_one), DataDefinitions.SIX_MONTHS);
        Spinner spinnerOverTime1 = (Spinner) findViewById(R.id.selectedChangeTime1);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOverTime1.setAdapter(adapterRange);
        spinnerOverTime1.setSelection(ranges.get(defaultOverTime1));
        spinnerOverTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_one), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int defaultOverTime2 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_two), DataDefinitions.MAX);
        Spinner spinnerOverTime2 = (Spinner) findViewById(R.id.selectedChangeTime2);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOverTime2.setAdapter(adapterRange);
        spinnerOverTime2.setSelection(ranges.get(defaultOverTime2));
        spinnerOverTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_two), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefToken = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                Intent intent = new Intent(Settings.this, Main.class);
                Utils.logout(sharedPrefToken, Settings.this, intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        Long tokenTime = mSharedPreferences.getLong(getString(R.string.token_date_preference), 0);
        if(System.currentTimeMillis() - tokenTime > TimeConversions.TOKEN_REFRESH_TIME){
            Intent intent = new Intent(Settings.this, Main.class);
            Utils.refreshToken(mSharedPreferences, Settings.this, intent);
        }
    }
}
