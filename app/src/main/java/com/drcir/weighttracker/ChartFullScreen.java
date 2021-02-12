package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;

    Button selectedButton = null;

    Button viewOneWeek;
    Button viewOneMonth;
    Button viewThreeMonth;
    Button viewSixMonth;
    Button viewYear;
    Button viewYtd;
    Button viewMax;

    TextView chartTitle;
    ProgressBar pBar;
    SimpleDateFormat mFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_chart_fullscreen);
        Type listType = new TypeToken<ArrayList<WeightEntry>>(){}.getType();

        try {
            Bundle b = getIntent().getExtras();
            DataString p = (DataString) b.getParcelable("dataSet");
            String d = p.getDataset();
            dataSet = new Gson().fromJson(d, listType);
            dataReceived();
        }catch (Exception e){
            getWeights();
        }

        chartTitle = findViewById(R.id.chartTitle);
        pBar = findViewById(R.id.pBar);
        mFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

        viewOneWeek = findViewById(R.id.viewOneWeek);
        viewOneMonth = findViewById(R.id.viewOneMonth);
        viewThreeMonth = findViewById(R.id.viewThreeMonth);
        viewSixMonth = findViewById(R.id.viewSixMonth);
        viewYear = findViewById(R.id.viewYear);
        viewYtd = findViewById(R.id.viewYtd);
        viewMax = findViewById(R.id.viewMax);

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
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(Utils.checkConnection(this, getString(R.string.no_connection_message))) {
            mCurrentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (!task.isSuccessful()) {
                            Intent intent = new Intent(ChartFullScreen.this, Main.class);
                            Utils.logout(ChartFullScreen.this, intent);
                        }
                    }
                });
        }
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
                    cal.setTime(new Date(dataSet.get(dataSet.size()-1).getDate()));
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
        if(timeMillis == 0)
            chartTitleLow = 0;
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
        }

        float chartLow = chart.getLowestVisibleX();
        float chartHigh = chart.getHighestVisibleX();

        //Calculate chart title dates
        if(chartHigh - chartLow < currentViewSize + .05) {
            if(chartLow < .1){
                chartLow = 0;
                chartHigh = currentViewSize;
                if(chartHigh > chartMaxValue)
                    chartHigh = chartMaxValue;
            }
            else{
                if(chartHigh > chartMaxValue)
                    chartHigh = chartMaxValue;
                chartLow = chartHigh - currentViewSize + .1F;;
                if(chartHigh < 0)
                    chartLow = 0;
            }
        }
        currentViewSize = chartHigh - chartLow;
        updateTitle(chartLow, chartHigh);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        float chartLow = chart.getLowestVisibleX();
        float chartHigh = chart.getHighestVisibleX();

        //Calculate chart title dates
        if (chartLow < .1) {
            chartLow = 0;
            chartHigh = currentViewSize;
        }
        else if (chartHigh > chartMaxValue - .01){
            chartHigh = chartMaxValue;
            chartLow = chartHigh - currentViewSize + .1F;
        }

        updateTitle(chartLow, chartHigh);
    }

    public void updateTitle(float chartLow, float chartHigh){
        String high = mFormat.format(dataStartDate + TimeConversions.ONE_DAY_MILLI * 10 * (chartHigh + .01F));
        String low = mFormat.format(dataStartDate + TimeConversions.ONE_DAY_MILLI * 10 * chartLow);
        String dateRange = low + " - " + high;
        chartTitle.setText(dateRange);
    }

    public void getWeights(){
        if(Utils.checkConnection(this, getString(R.string.no_connection_message))) {
            mCurrentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                            Call<List<WeightEntry>> call = apiInterface.getWeightData(token);
                            call.enqueue(new Callback<List<WeightEntry>>() {
                                @Override
                                public void onResponse(Call<List<WeightEntry>> call, Response<List<WeightEntry>> response) {
                                    if (response.isSuccessful()) {
                                        dataSet = response.body();
                                        dataReceived();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<WeightEntry>> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        }
                    }
                });
        }
    }

    public void dataReceived(){
        dataSetLength = dataSet.size() *.1f;
        dataStartDate = dataSet.get(0).getDate() / TimeConversions.ONE_DAY_MILLI * TimeConversions.ONE_DAY_MILLI + TimeConversions.ONE_DAY_MILLI / 2;
        sharedPrefRange = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);

        //Set Chart Entries
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < dataSet.size(); i++){
            entries.add(i, new Entry(i * .1f, dataSet.get(i).getWeight()));
        }
        LineDataSet entrySet = new LineDataSet(entries, null); // add entries to dataset

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
        int defaultRange;
        //Set Chart Range to Preference Size, default Max
        try {
            SharedPreferences mRangePref = getSharedPreferences(getString(R.string.range_preferences), Context.MODE_PRIVATE);
            defaultRange = ranges.get(mRangePref.getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX));
        }
        catch (Exception e){
            defaultRange = DataDefinitions.MAX;
        }
        setViewport((Button) findViewById(defaultRange));
        pBar.setVisibility(View.INVISIBLE);
        chart.setVisibility(View.VISIBLE);
    }

    public void stopChartMovement(){
        chart.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
    }
}