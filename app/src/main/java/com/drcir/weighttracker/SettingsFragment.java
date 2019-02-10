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

    private BaseActivityListener baseActivityListener;
    ArrayAdapter<CharSequence> adapterRange;
    int defaultOverTime1;
    int defaultOverTime2;
    SparseIntArray ranges;

    Spinner spinnerOverTime1;
    Spinner spinnerOverTime2;
    TextView logout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            baseActivityListener = (BaseActivityListener) context;
        } catch (ClassCastException castException) {
        }
    }

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

        SharedPreferences sharedPrefRange = baseActivityListener.getRangePref();
        defaultOverTime1 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_one), DataDefinitions.SIX_MONTHS);
        defaultOverTime2 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_two), DataDefinitions.MAX);

        adapterRange = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.chart_ranges, android.R.layout.simple_spinner_item);
        adapterRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        spinnerOverTime1 = rootView.findViewById(R.id.selectedChangeTime1);
        spinnerOverTime2 = rootView.findViewById(R.id.selectedChangeTime2);
        logout = rootView.findViewById(R.id.logout);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerOverTime1.setAdapter(adapterRange);
        spinnerOverTime1.setSelection(ranges.get(defaultOverTime1));
        spinnerOverTime1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences.Editor mEditorRange = baseActivityListener.getRangePref().edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_one), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOverTime2.setAdapter(adapterRange);
        spinnerOverTime2.setSelection(ranges.get(defaultOverTime2));
        spinnerOverTime2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int range = ranges.keyAt(ranges.indexOfValue(position));
                SharedPreferences.Editor mEditorRange = baseActivityListener.getRangePref().edit();
                mEditorRange.putInt(getResources().getString(R.string.chart_over_time_preference_two), range).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main.class);
                Utils.logout(getActivity(), intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
