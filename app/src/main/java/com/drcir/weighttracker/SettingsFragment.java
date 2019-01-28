package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.drcir.weighttracker.data.DataDefinitions;

public class SettingsFragment extends Fragment {
    ArrayAdapter<CharSequence> adapterRange;
    int defaultChartRange;
    int defaultOverTime1;
    int defaultOverTime2;
    SparseIntArray ranges;
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        ranges = new SparseIntArray();
        ranges.append(DataDefinitions.ONE_WEEK, 0);
        ranges.append(DataDefinitions.ONE_MONTH, 1);
        ranges.append(DataDefinitions.THREE_MONTHS, 2);
        ranges.append(DataDefinitions.SIX_MONTHS, 3);
        ranges.append(DataDefinitions.ONE_YEAR, 4);
        ranges.append(DataDefinitions.YTD, 5);
        ranges.append(DataDefinitions.MAX, 6);

        SharedPreferences sharedPrefRange = this.getActivity().getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
        defaultChartRange = sharedPrefRange.getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX);
        defaultOverTime1 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_one), DataDefinitions.SIX_MONTHS);
        defaultOverTime2 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_two), DataDefinitions.MAX);

        adapterRange = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.chart_ranges, android.R.layout.simple_spinner_item);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerChartRange = getView().findViewById(R.id.selectedRange);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChartRange.setAdapter(adapterRange);
        spinnerChartRange.setSelection(ranges.get(defaultChartRange));
        spinnerChartRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = SettingsFragment.this.getActivity().getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_range_preference), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerOverTime1 = getView().findViewById(R.id.selectedChangeTime1);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOverTime1.setAdapter(adapterRange);
        spinnerOverTime1.setSelection(ranges.get(defaultOverTime1));
        spinnerOverTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = SettingsFragment.this.getActivity().getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_one), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerOverTime2 = getView().findViewById(R.id.selectedChangeTime2);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOverTime2.setAdapter(adapterRange);
        spinnerOverTime2.setSelection(ranges.get(defaultOverTime2));
        spinnerOverTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences sharedPrefRange = SettingsFragment.this.getActivity().getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditorRange = sharedPrefRange.edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_two), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView logout = getView().findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefToken = SettingsFragment.this.getActivity().getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                Intent intent = new Intent(SettingsFragment.this.getActivity(), Main.class);
                Utils.logout(sharedPrefToken, SettingsFragment.this.getActivity(), intent);
            }
        });



    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
