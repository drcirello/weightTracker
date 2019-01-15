package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drcir.weighttracker.data.DataDefinitions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Charts extends AppCompatActivity {

    LineChart chart;
    XAxis xAxis;
    List<WeightEntry> dataSet;
    //Maximum chartSize
    float chartMaxSize;
    Button selectedButton = null;
    ChartStatistics stats;
    RelativeLayout chartFrame;
    String token;
    //Passed to Fullscreen Chart
    int selectedButtonRange;

    Button viewOneWeek;
    Button viewOneMonth;
    Button viewThreeMonth;
    Button viewSixMonth;
    Button viewYear;
    Button viewYtd;
    Button viewMax;

    TextView charts_current_weight;
    TextView charts_high_response;
    TextView charts_low_response;
    TextView charts_year_high_response;
    TextView charts_year_low_response;
    TextView charts_six_mo_change_response;
    TextView charts_year_change_response;
    LinearLayout charts_failed_message;

    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_charts);
        Toolbar myTitlebar = findViewById(R.id.titleBar);
        setSupportActionBar(myTitlebar);
        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_charts);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();

                        Intent intent;
                        switch (id) {
                            case R.id.action_view:
                                intent = new Intent(Charts.this, WeightEntries.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_create:
                                intent = new Intent(Charts.this, EnterWeight.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(Charts.this, Settings.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_charts:
                            default:
                                break;
                        }
                        return false;
                    }
                });

        SharedPreferences sharedPrefToken = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        token = sharedPrefToken.getString(getString(R.string.token_preference), null);

        getWeightEntries(token);

        viewOneWeek = findViewById(R.id.viewOneWeek);
        viewOneMonth = findViewById(R.id.viewOneMonth);
        viewThreeMonth = findViewById(R.id.viewThreeMonth);
        viewSixMonth = findViewById(R.id.viewSixMonth);
        viewYear = findViewById(R.id.viewYear);
        viewYtd = findViewById(R.id.viewYtd);
        viewMax = findViewById(R.id.viewMax);

        charts_current_weight = findViewById(R.id.currentWeight);
        charts_high_response = findViewById(R.id.charts_high_response);
        charts_low_response = findViewById(R.id.charts_low_response);
        charts_year_high_response = findViewById(R.id.charts_year_high_response);
        charts_year_low_response = findViewById(R.id.charts_year_low_response);
        charts_six_mo_change_response = findViewById(R.id.charts_six_mo_change_response);
        charts_year_change_response = findViewById(R.id.charts_year_change_response);
        charts_failed_message = findViewById(R.id.failedMessage);
        pBar = findViewById(R.id.pBar);

        chart = findViewById(R.id.lineChart);
        chartFrame = findViewById(R.id.chartFrame);
        //Chart Button Handling
        viewOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewOneWeek);
            }
        });

        viewOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewOneMonth);
            }
        });

        viewThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewThreeMonth);
            }
        });

        viewSixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewSixMonth);
            }
        });

        viewYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewYear);
            }
        });

        viewYtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewYtd);
            }
        });

        viewMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewport(viewMax);
            }
        });

    }

    public void maxView(){
        chart.fitScreen();
        ChartUtils.setXaxisScale(xAxis, chartMaxSize);
        selectedButtonRange = DataDefinitions.MAX;
    }

    public void setInitialViewport(){
        Map<Integer, Integer> ranges = new HashMap<Integer, Integer>();
        ranges.put(DataDefinitions.ONE_WEEK, R.id.viewOneWeek);
        ranges.put(DataDefinitions.ONE_MONTH, R.id.viewOneMonth);
        ranges.put(DataDefinitions.THREE_MONTHS, R.id.viewThreeMonth);
        ranges.put(DataDefinitions.SIX_MONTHS, R.id.viewSixMonth);
        ranges.put(DataDefinitions.ONE_YEAR, R.id.viewYear);
        ranges.put(DataDefinitions.YTD, R.id.viewYtd);
        ranges.put(DataDefinitions.MAX, R.id.viewMax);
        int defaultRange;
        //Set Chart Range to Preference Size, default Max
        try {
            SharedPreferences sharedPrefUnit = getSharedPreferences(getString(R.string.unit_preferences), Context.MODE_PRIVATE);
            defaultRange = sharedPrefUnit.getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX);
        }
        catch (Exception e){
            defaultRange = DataDefinitions.MAX;
        }

        int resource_id = ranges.get(defaultRange);
        try {
            setViewport((Button) findViewById(resource_id));
        }
        catch (Exception e){
            setViewport(viewMax);
        }
    }

    public void setViewport(Button button){
        if(dataSet != null) {
            if (selectedButton != null)
                selectedButton.setBackgroundColor(ContextCompat.getColor(Charts.this, R.color.colorChartButtonBackground));
            selectedButton = button;
            button.setBackgroundColor(ContextCompat.getColor(Charts.this, R.color.colorTitleBar));
            switch (button.getId()) {
                case R.id.viewOneWeek:
                    if (chartMaxSize < TimeConversions.SEVEN_DAYS_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.SEVEN_DAYS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.SEVEN_DAYS_MILLI);
                        selectedButtonRange = DataDefinitions.ONE_WEEK;
                    }
                    break;
                case R.id.viewOneMonth:
                    if (chartMaxSize < TimeConversions.ONE_MONTH_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.ONE_MONTH_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.ONE_MONTH_MILLI);
                        selectedButtonRange = DataDefinitions.ONE_MONTH;
                    }
                    break;
                case R.id.viewThreeMonth:
                    if (chartMaxSize < TimeConversions.THREE_MONTHS_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.THREE_MONTHS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.THREE_MONTHS_MILLI);
                        selectedButtonRange = DataDefinitions.THREE_MONTHS;
                    }
                    break;
                case R.id.viewSixMonth:
                    if (chartMaxSize < TimeConversions.SIX_MONTHS_MILLI + TimeConversions.ONE_DAY_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.SIX_MONTHS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.SIX_MONTHS_MILLI);
                        selectedButtonRange = DataDefinitions.SIX_MONTHS;
                    }
                    break;
                case R.id.viewYear:
                    if (chartMaxSize < TimeConversions.ONE_YEAR_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.ONE_YEAR_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.ONE_YEAR_MILLI);
                        selectedButtonRange = DataDefinitions.ONE_YEAR;
                    }
                    break;
                case R.id.viewYtd:
                    Calendar cal = Calendar.getInstance();
                    int days = cal.get(cal.DAY_OF_YEAR);
                    float ytd = days * TimeConversions.ONE_DAY_MILLI;
                    if (chartMaxSize < ytd)
                        maxView();
                    else {
                        ChartUtils.updateChartViewport(chart, ytd);
                        ChartUtils.setXaxisScale(xAxis, ytd);
                        selectedButtonRange = DataDefinitions.YTD;
                    }
                    break;
                case R.id.viewMax:
                    maxView();
                default:
                    break;
            }
        }
    }



    public void setChartStatistics(){
        stats = new ChartStatistics(dataSet);
        DecimalFormat form = Utils.getDecimalFormat();
        charts_current_weight.setText(form.format(stats.getCurrentWeight()));
        charts_high_response.setText(form.format(stats.getHighMax()));
        charts_low_response.setText(form.format(stats.getLowMax()));
        charts_year_high_response.setText(form.format(stats.getHigh1y()));
        charts_year_low_response.setText(form.format(stats.getLow1y()));
        charts_six_mo_change_response.setText(form.format(stats.getChange6mo()));
        charts_year_change_response.setText(form.format(stats.getChange1y()));
    }

    public void createChart(){
        //Set Chart Entries
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < dataSet.size(); i++){
            entries.add(i, new Entry((float)dataSet.get(i).getDate(), dataSet.get(i).getWeight()));
        }
        LineDataSet entrySet = new LineDataSet(entries, null); // add entries to dataset

        //entriesSet options
        entrySet.setDrawValues(false);
        entrySet.setDrawFilled(true);
        entrySet.setDrawCircles(false);

        LineData lineData = new LineData(entrySet);
        //chart options
        //Disable Chart Interactions
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        //Axis options
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        xAxis  = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        chart.setData(lineData);
        chartMaxSize = chart.getHighestVisibleX();
        chartFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Charts.this, ChartFullScreen.class);
                Gson gson = new Gson();
                String dataSetString = gson.toJson(dataSet);
                intent.putExtra("DATASET", dataSetString);
                intent.putExtra("SELECTED_BUTTON", Integer.toString(selectedButtonRange));
                startActivity(intent);
            }
        });
    }

    public void removeLoadingScreen(){
        chart.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.GONE);
        charts_failed_message.setVisibility(View.GONE);
        LinearLayout buttonBar = findViewById(R.id.buttonBar);
        buttonBar.setVisibility(View.VISIBLE);
    }

    public void dataRetrievalFailure(){
        String failedText = getString(R.string.charts_api_failed_fields);
        charts_current_weight.setText(failedText);
        charts_high_response.setText(failedText);
        charts_low_response.setText(failedText);
        charts_year_high_response.setText(failedText);
        charts_year_low_response.setText(failedText);
        charts_six_mo_change_response.setText(failedText);
        charts_year_change_response.setText(failedText);

        chart.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.GONE);
        charts_failed_message.setVisibility(View.VISIBLE);
        chartFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightEntries(token);
            }
        });
    }

    public void getWeightEntries(String token){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<WeightEntry>> call = apiInterface.getWeightData(token);
        call.enqueue(new Callback<List<WeightEntry>>() {
            @Override
            public void onResponse(Call<List<WeightEntry>> call, Response<List<WeightEntry>> response) {
                if(response.isSuccessful()) {
                    pBar.setVisibility(View.VISIBLE);
                    charts_failed_message.setVisibility(View.GONE);
                    dataSet = response.body();
                    setChartStatistics();
                    createChart();
                    setInitialViewport();
                    removeLoadingScreen();
                }
            }

            @Override
            public void onFailure(Call<List<WeightEntry>> call, Throwable t) {
                call.cancel();
                dataRetrievalFailure();
            }
        });
    };

}
