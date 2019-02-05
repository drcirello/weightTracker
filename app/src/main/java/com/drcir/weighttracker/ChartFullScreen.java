package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drcir.weighttracker.data.DataDefinitions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartFullScreen extends AppCompatActivity implements OnChartGestureListener {

    List<WeightEntry> dataSet;
    LineChart chart;
    XAxis xAxis;
    //Chart min/max view position
    float chartMaxValue;
    long dataStartDate;
    float dataSetLength;
    float currentViewSize;
    SharedPreferences sharedPrefRange;

    Button selectedButton = null;

    Button viewOneWeek;
    Button viewOneMonth;
    Button viewThreeMonth;
    Button viewSixMonth;
    Button viewYear;
    Button viewYtd;
    Button viewMax;

    TextView chartTitle;
    SimpleDateFormat mFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_fullscreen);
        Type listType = new TypeToken<ArrayList<WeightEntry>>(){}.getType();
        dataSet = new Gson().fromJson(getIntent().getStringExtra("DATASET"), listType);
        dataSetLength = dataSet.size() *.1f;
        dataStartDate = dataSet.get(0).getDate() / TimeConversions.ONE_DAY_MILLI * TimeConversions.ONE_DAY_MILLI + TimeConversions.ONE_DAY_MILLI / 2;
        sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);

        chartTitle = findViewById(R.id.chartTitle);
        mFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

        //Set Chart Entries
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < dataSet.size(); i++){
            entries.add(i, new Entry(i * .1f, dataSet.get(i).getWeight()));
        }
        LineDataSet entrySet = new LineDataSet(entries, null); // add entries to dataset

        viewOneWeek = findViewById(R.id.viewOneWeek);
        viewOneMonth = findViewById(R.id.viewOneMonth);
        viewThreeMonth = findViewById(R.id.viewThreeMonth);
        viewSixMonth = findViewById(R.id.viewSixMonth);
        viewYear = findViewById(R.id.viewYear);
        viewYtd = findViewById(R.id.viewYtd);
        viewMax = findViewById(R.id.viewMax);

        chart = findViewById(R.id.lineChart);
        //entriesSet options
        entrySet.setFillFormatter(new CustomFillFormatter());
        entrySet.setDrawValues(false);
        entrySet.setDrawFilled(true);
        entrySet.setDrawCircles(false);

        LineData lineData = new LineData(entrySet);
        //chart options
        chart.getDescription().setEnabled(false);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        //Axis options
        YAxis rightAxis = chart.getAxisRight();
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(1);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawZeroLine(true);
        rightAxis.setEnabled(false);

        xAxis  = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        chart.setData(lineData);
        chartMaxValue = chart.getHighestVisibleX();
        //more chart options
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.colorChartBorder));
        chart.setBorderWidth(1);
        chart.setExtraOffsets(2, 0, 20, 0);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setOnChartGestureListener(this);
        chart.setHardwareAccelerationEnabled(true);
        currentViewSize = chart.getHighestVisibleX() - chart.getLowestVisibleX();

        Map<Integer, Integer> ranges = new HashMap<Integer, Integer>();
        ranges.put(DataDefinitions.ONE_WEEK, R.id.viewOneWeek);
        ranges.put(DataDefinitions.ONE_MONTH, R.id.viewOneMonth);
        ranges.put(DataDefinitions.THREE_MONTHS, R.id.viewThreeMonth);
        ranges.put(DataDefinitions.SIX_MONTHS, R.id.viewSixMonth);
        ranges.put(DataDefinitions.ONE_YEAR, R.id.viewYear);
        ranges.put(DataDefinitions.YTD, R.id.viewYtd);
        ranges.put(DataDefinitions.MAX, R.id.viewMax);

        try {
            int resource_id = ranges.get(Integer.parseInt(getIntent().getStringExtra("SELECTED_BUTTON")));
            setViewport((Button) findViewById(resource_id));
        }
        catch (Exception e){
            setViewport(viewMax);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshToken();
    }

    public void setViewport(Button button){
        if(dataSet != null) {
            if (selectedButton != null)
                selectedButton.setBackgroundColor(ContextCompat.getColor(ChartFullScreen.this, R.color.colorChartButtonBackground));
            selectedButton = button;
            button.setBackgroundColor(ContextCompat.getColor(ChartFullScreen.this, R.color.colorTitleBar));
            switch (button.getId()) {
                case R.id.viewOneWeek:
                    chartButtonPressed(TimeConversions.SEVEN_DAYS_FLOAT, DataDefinitions.ONE_WEEK);
                    break;
                case R.id.viewOneMonth:
                    chartButtonPressed(TimeConversions.ONE_MONTH_FLOAT, DataDefinitions.ONE_MONTH);
                    break;
                case R.id.viewThreeMonth:
                    chartButtonPressed(TimeConversions.THREE_MONTHS_FLOAT, DataDefinitions.THREE_MONTHS);
                    break;
                case R.id.viewSixMonth:
                    chartButtonPressed(TimeConversions.SIX_MONTHS_FLOAT, DataDefinitions.SIX_MONTHS);
                    break;
                case R.id.viewYear:
                    chartButtonPressed(TimeConversions.ONE_YEAR_FLOAT, DataDefinitions.ONE_YEAR);
                    break;
                case R.id.viewYtd:
                    Calendar cal = Calendar.getInstance();
                    int days = cal.get(Calendar.DAY_OF_YEAR);
                    float ytd = (days - 1) * TimeConversions.ONE_DAY_FLOAT;
                    chartButtonPressed(ytd, DataDefinitions.YTD);
                    break;
                case R.id.viewMax:
                    chartButtonPressed(DataDefinitions.MAX, DataDefinitions.MAX);
                default:
                    break;
            }
        }
    }

    public void chartButtonPressed(float timeMillis, int selectedbutton){
        stopChartMovement();
        boolean ytd = false;
        if(selectedbutton == DataDefinitions.YTD)
            ytd = true;
        currentViewSize = timeMillis;
        float chartTitleHigh = chart.getHighestVisibleX();
        if(ytd){
            chartTitleHigh = chartMaxValue;
        }
        float chartTitleLow = chartTitleHigh - timeMillis;
        updateTitle(chartTitleLow, chartTitleHigh);
        sharedPrefRange.edit().putInt(getResources().getString(R.string.chart_range_preference), selectedbutton).apply();
        if(dataSet.size() != 1) {
            if (chartMaxValue < timeMillis || timeMillis == 0) {
                chart.fitScreen();
                timeMillis = chartMaxValue;
            } else {

                chart = ChartUtils.updateChartViewportFullscreen(chart, timeMillis, ytd);
            }
        }
        xAxis = ChartUtils.setXaxisScale(xAxis, timeMillis, dataStartDate);
    }

    public void refreshToken(){
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        Long tokenTime = mSharedPreferences.getLong(getString(R.string.token_date_preference), 0);
        if(System.currentTimeMillis() - tokenTime > TimeConversions.TOKEN_REFRESH_TIME){
            Intent intent = new Intent(ChartFullScreen.this, Main.class);
            Utils.refreshToken(mSharedPreferences, ChartFullScreen.this, intent);
        }
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        float highViewX = chart.getHighestVisibleX();
        float lowViewX = chart.getLowestVisibleX();
        if(highViewX > dataSetLength - .1)
            highViewX = dataSetLength;

        float newViewSize = highViewX - lowViewX;
        float rangeDifference = currentViewSize - newViewSize;

        if(rangeDifference <= -TimeConversions.ONE_DAY_FLOAT || rangeDifference >= TimeConversions.ONE_DAY_FLOAT) {
            xAxis = ChartUtils.setXaxisScale(xAxis, highViewX - lowViewX, dataStartDate);
            currentViewSize = newViewSize;
        }
        updateTitle(chart.getLowestVisibleX(), chart.getHighestVisibleX());
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        updateTitle(chart.getLowestVisibleX(), chart.getHighestVisibleX());
    }

    public void updateTitle(float chartLow, float chartHigh){
        if(chartHigh - chartLow < currentViewSize + .05) {
            if(chartLow <= 0){
                chartLow = 0;
                chartHigh = currentViewSize;
            }
            else{
                if(chartHigh > chartMaxValue)
                    chartHigh = chartMaxValue;
                chartLow = chartHigh - currentViewSize;
            }
        }
        String high = mFormat.format(dataStartDate + TimeConversions.ONE_DAY_MILLI * 10 * (chartHigh + .01F));
        String low = mFormat.format(dataStartDate + TimeConversions.ONE_DAY_MILLI * 10 * chartLow);
        String dateRange = low + " - " + high;
        chartTitle.setText(dateRange);
    }

    public void stopChartMovement(){
        chart.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
    }
}