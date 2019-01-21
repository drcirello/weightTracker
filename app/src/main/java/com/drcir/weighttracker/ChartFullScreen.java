package com.drcir.weighttracker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartFullScreen extends AppCompatActivity implements OnChartGestureListener {

    List<WeightEntry> dataSet;
    LineChart chart;
    XAxis xAxis;
    //Chart min/max view position
    float chartMinValue;
    float chartMaxValue;

    Button selectedButton = null;

    Button viewOneWeek;
    Button viewOneMonth;
    Button viewThreeMonth;
    Button viewSixMonth;
    Button viewYear;
    Button viewYtd;
    Button viewMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_fullscreen);
        Type listType = new TypeToken<ArrayList<WeightEntry>>(){}.getType();
        dataSet = new Gson().fromJson(getIntent().getStringExtra("DATASET"), listType);

        //Set Chart Entries
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < dataSet.size(); i++){
            entries.add(i, new Entry((float)dataSet.get(i).getDate(), dataSet.get(i).getWeight()));
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
        rightAxis.setEnabled(false);

        xAxis  = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        chart.setData(lineData);
        chartMinValue = entries.get(0).getX();
        chartMaxValue = chart.getHighestVisibleX();
        //more chart options
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.colorChartBorder));
        chart.setBorderWidth(1);
        chart.setExtraLeftOffset(2f);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setOnChartGestureListener(this);

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

    public void setViewport(Button button){
        if(dataSet != null) {
            if (selectedButton != null)
                selectedButton.setBackgroundColor(ContextCompat.getColor(ChartFullScreen.this, R.color.colorChartButtonBackground));
            selectedButton = button;
            button.setBackgroundColor(ContextCompat.getColor(ChartFullScreen.this, R.color.colorTitleBar));
            switch (button.getId()) {
                case R.id.viewOneWeek:
                    chartButtonPressed(TimeConversions.SEVEN_DAYS_MILLI);
                    break;
                case R.id.viewOneMonth:
                    chartButtonPressed(TimeConversions.ONE_MONTH_MILLI);
                    break;
                case R.id.viewThreeMonth:
                    chartButtonPressed(TimeConversions.THREE_MONTHS_MILLI);
                    break;
                case R.id.viewSixMonth:
                    chartButtonPressed(TimeConversions.SIX_MONTHS_MILLI);
                    break;
                case R.id.viewYear:
                    chartButtonPressed(TimeConversions.ONE_YEAR_MILLI);
                    break;
                case R.id.viewYtd:
                    Calendar cal = Calendar.getInstance();
                    int days = cal.get(cal.DAY_OF_YEAR);
                    float ytd = (days - 1) * TimeConversions.ONE_DAY_MILLI;
                    chartButtonPressed(ytd);
                    break;
                case R.id.viewMax:
                    maxView();
                default:
                    break;
            }
        }
    }

    public void chartButtonPressed(float timeMillis){
        if (chartMaxValue < timeMillis)
            maxView();
        else {
            chart = ChartUtils.updateChartViewportFullscreen(chart, timeMillis, chartMinValue);
            xAxis = ChartUtils.setXaxisScale(xAxis, timeMillis, true);
        }
    }

    public void maxView(){
        chart.fitScreen();
        ChartUtils.setXaxisScale(xAxis, chartMaxValue, true);
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        float highViewX = chart.getHighestVisibleX();
        float lowViewX = chart.getLowestVisibleX();
        xAxis = ChartUtils.setXaxisScale(xAxis, highViewX - lowViewX, true);
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

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}