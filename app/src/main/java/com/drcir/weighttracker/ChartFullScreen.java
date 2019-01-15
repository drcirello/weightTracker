package com.drcir.weighttracker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartFullScreen extends AppCompatActivity {

    List<WeightEntry> dataSet;
    LineChart chart;
    XAxis xAxis;
    //Maximum chartSize
    float chartMaxSize;

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
        //Disable Chart Interactions
        chart.setTouchEnabled(true);
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
                    if (chartMaxSize < TimeConversions.SEVEN_DAYS_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.SEVEN_DAYS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.SEVEN_DAYS_MILLI);
                    }
                    break;
                case R.id.viewOneMonth:
                    if (chartMaxSize < TimeConversions.ONE_MONTH_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.ONE_MONTH_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.ONE_MONTH_MILLI);
                    }
                    break;
                case R.id.viewThreeMonth:
                    if (chartMaxSize < TimeConversions.THREE_MONTHS_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.THREE_MONTHS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.THREE_MONTHS_MILLI);
                    }
                    break;
                case R.id.viewSixMonth:
                    if (chartMaxSize < TimeConversions.SIX_MONTHS_MILLI + TimeConversions.ONE_DAY_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.SIX_MONTHS_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.SIX_MONTHS_MILLI);
                    }
                    break;
                case R.id.viewYear:
                    if (chartMaxSize < TimeConversions.ONE_YEAR_MILLI)
                        maxView();
                    else{
                        chart = ChartUtils.updateChartViewport(chart, TimeConversions.ONE_YEAR_MILLI);
                        xAxis = ChartUtils.setXaxisScale(xAxis, TimeConversions.ONE_YEAR_MILLI);
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
                    }
                    break;
                case R.id.viewMax:
                    maxView();
                default:
                    break;
            }
        }
    }

    public void maxView(){
        chart.fitScreen();
        ChartUtils.setXaxisScale(xAxis, chartMaxSize);
    }
}

/*


    public void updateChartViewportFullscreen(float scale){
        //get current right side of viewport
        float highViewX = chart.getHighestVisibleX();
        //resets entire chart
        chart.fitScreen();
        //gets the maximum possible x value
        ViewPortHandler handler = chart.getViewPortHandler();
        float maxScale = handler.getMaxScaleX();
        //Sets viewport size for selected scale
        chart.setVisibleXRangeMaximum(scale);
        //Moves viewport back to current position
        chart.moveViewToX(highViewX - scale);
        //Allows pinch zooming to full range
        chart.setVisibleXRangeMaximum(maxScale);
    }
    */