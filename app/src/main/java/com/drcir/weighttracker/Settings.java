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
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        SharedPreferences sharedPrefUnit = getSharedPreferences(getString(R.string.unit_preferences), Context.MODE_PRIVATE);
        int defaultRange = sharedPrefUnit.getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX);
        Spinner spinnerRange = (Spinner) findViewById(R.id.selectedRange);
        ArrayAdapter<CharSequence> adapterRange = ArrayAdapter.createFromResource(this,
                R.array.chart_ranges, android.R.layout.simple_spinner_item);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRange.setAdapter(adapterRange);
        spinnerRange.setSelection(ranges.get(defaultRange));
        spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefUnit = getSharedPreferences(getString(R.string.unit_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorUnit = sharedPrefUnit.edit();
                mEditorUnit.putInt(getResources().getString(R.string.chart_range_preference), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SparseIntArray unitTypes = new SparseIntArray();
        unitTypes.append(DataDefinitions.POUNDS, 0);
        unitTypes.append(DataDefinitions.KILOGRAMS, 1);

        int defaultUnits = sharedPrefUnit.getInt(getString(R.string.unit_type_preference), DataDefinitions.POUNDS);
        Spinner spinnerUnits = (Spinner) findViewById(R.id.selectedUnitType);
        ArrayAdapter<CharSequence> adapterUnits = ArrayAdapter.createFromResource(this,
                R.array.unit_types, android.R.layout.simple_spinner_item);
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapterUnits);
        spinnerUnits.setSelection(unitTypes.get(defaultUnits));
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int unitType = unitTypes.keyAt(unitTypes.indexOfValue(position));
                SharedPreferences sharedPrefUnit = getSharedPreferences(getString(R.string.unit_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorUnit = sharedPrefUnit.edit();
                mEditorUnit.putInt(getResources().getString(R.string.unit_type_preference), unitType).apply();
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
}
