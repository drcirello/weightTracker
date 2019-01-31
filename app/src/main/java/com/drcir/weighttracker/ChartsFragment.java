package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drcir.weighttracker.data.DataDefinitions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartsFragment extends Fragment {

    private BaseActivityListener baseActivityListener;

    LineChart chart;
    XAxis xAxis;
    List<WeightEntry> mDataSet;
    float chartMaxSize;

    Map<Integer, Integer> ranges;
    int selectedButtonRange;
    int defaultOverTime1;
    int defaultOverTime2;
    long startDate;

    Button selectedButton;
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
    TextView charts_change_over_time_1_response;
    TextView charts_change_over_time_2_response;
    TextView charts_change_over_time_1;
    TextView charts_change_over_time_2;
    LinearLayout charts_failed_message;

    View divider;
    TextView messageText;
    LinearLayout buttonBar;
    LinearLayout mainView;
    RelativeLayout noDataView;
    RelativeLayout chartFrame;
    ImageView expandButton;
    ImageView noDataImage;
    ProgressBar pBar;

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
        selectedButton = null;
        ranges = new HashMap<Integer, Integer>();
        baseActivityListener.getTokenPref().getString(getString(R.string.token_JWT_preference), null);
        SharedPreferences sharedPrefRange = baseActivityListener.getRangePref();
        defaultOverTime1 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_one), DataDefinitions.SIX_MONTHS);
        defaultOverTime2 = sharedPrefRange.getInt(getString(R.string.chart_over_time_preference_two), DataDefinitions.MAX);

        ranges.put(DataDefinitions.ONE_WEEK, R.string.over_time_one_week);
        ranges.put(DataDefinitions.ONE_MONTH, R.string.over_time_one_month);
        ranges.put(DataDefinitions.THREE_MONTHS, R.string.over_time_three_month);
        ranges.put(DataDefinitions.SIX_MONTHS, R.string.over_time_six_month);
        ranges.put(DataDefinitions.ONE_YEAR, R.string.over_time_one_year);
        ranges.put(DataDefinitions.YTD, R.string.over_time_ytd);
        ranges.put(DataDefinitions.MAX, R.string.over_time_max);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);

        viewOneWeek = rootView.findViewById(R.id.viewOneWeek);
        viewOneMonth = rootView.findViewById(R.id.viewOneMonth);
        viewThreeMonth = rootView.findViewById(R.id.viewThreeMonth);
        viewSixMonth = rootView.findViewById(R.id.viewSixMonth);
        viewYear = rootView.findViewById(R.id.viewYear);
        viewYtd = rootView.findViewById(R.id.viewYtd);
        viewMax = rootView.findViewById(R.id.viewMax);

        charts_current_weight = rootView.findViewById(R.id.currentWeight);
        charts_high_response = rootView.findViewById(R.id.charts_high_response);
        charts_low_response = rootView.findViewById(R.id.charts_low_response);
        charts_year_high_response = rootView.findViewById(R.id.charts_year_high_response);
        charts_year_low_response = rootView.findViewById(R.id.charts_year_low_response);
        charts_change_over_time_1 = rootView.findViewById(R.id.charts_change_over_time_1);
        charts_change_over_time_2 = rootView.findViewById(R.id.charts_change_over_time_2);
        charts_change_over_time_1_response = rootView.findViewById(R.id.charts_change_over_time_1_response);
        charts_change_over_time_2_response = rootView.findViewById(R.id.charts_change_over_time_2_response);

        charts_failed_message = rootView.findViewById(R.id.failedMessage);
        pBar = rootView.findViewById(R.id.pBar);
        chart = rootView.findViewById(R.id.lineChart);
        chartFrame = rootView.findViewById(R.id.chartFrame);
        expandButton = rootView.findViewById(R.id.expand_button);
        divider = rootView.findViewById(R.id.chartDivider);
        buttonBar = rootView.findViewById(R.id.buttonBar);
        messageText = rootView.findViewById(R.id.charts_api_failed_message_part1);
        mainView = rootView.findViewById(R.id.mainView);
        noDataView = rootView.findViewById(R.id.noData);
        noDataImage = rootView.findViewById(R.id.noDataImage);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        charts_change_over_time_1.setText(getString(ranges.get(defaultOverTime1)));
        charts_change_over_time_2.setText(getString(ranges.get(defaultOverTime2)));
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
    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        if(baseActivityListener.getUpdateDataSetCharts()) {
            getWeightEntries(baseActivityListener.getTokenPref().getString(getString(R.string.token_JWT_preference), null));
        }
        else {
            mDataSet = baseActivityListener.getDataSetCharts();
            dataSetReceived();
        }
        super.onResume();
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
            defaultRange = baseActivityListener.getRangePref().getInt(getString(R.string.chart_range_preference), DataDefinitions.MAX);
        }
        catch (Exception e){
            defaultRange = DataDefinitions.MAX;
        }

        int resource_id = ranges.get(defaultRange);
        try {
            setViewport((Button) getView().findViewById(resource_id));
        }
        catch (Exception e){
            setViewport(viewMax);
        }
    }

    public void setViewport(Button button){
        if(mDataSet != null) {
            if (selectedButton != null)
                selectedButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorChartButtonBackground));
            selectedButton = button;
            button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTitleBar));
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
                    float ytd = days * TimeConversions.ONE_DAY_FLOAT;
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
        if(mDataSet.size() != 1) {
            if (chartMaxSize < timeMillis || timeMillis == DataDefinitions.MAX){
                chart.fitScreen();
                timeMillis = chartMaxSize;
            }
            else {
                chart = ChartUtils.updateChartViewport(chart, timeMillis);
            }
        }
        xAxis = ChartUtils.setXaxisScale(xAxis, timeMillis, startDate);
        selectedButtonRange = selectedbutton;
    }

    public void setChartStatistics(){
        ChartStatistics stats = new ChartStatistics(mDataSet, defaultOverTime1, defaultOverTime2);
        DecimalFormat form = Utils.getDecimalFormat();
        charts_current_weight.setText(form.format(stats.getCurrentWeight()));
        charts_high_response.setText(form.format(stats.getHighMax()));
        charts_low_response.setText(form.format(stats.getLowMax()));
        charts_year_high_response.setText(form.format(stats.getHigh1y()));
        charts_year_low_response.setText(form.format(stats.getLow1y()));
        charts_change_over_time_1_response.setText(form.format(stats.getChangeOverTime1()));
        charts_change_over_time_2_response.setText(form.format(stats.getChangeOverTime2()));
    }

    public void setFakeChartStatistics(){
        charts_current_weight.setText(R.string.no_data_current_weight);
        charts_high_response.setText(R.string.no_data_high);
        charts_low_response.setText(R.string.no_data_low);
        charts_year_high_response.setText(R.string.no_data_year_high);
        charts_year_low_response.setText(R.string.no_data_year_low);
        charts_change_over_time_1_response.setText(R.string.no_data_six_mo_change);
        charts_change_over_time_2_response.setText(R.string.no_data_year_change);
        charts_change_over_time_1.setText(R.string.over_time_six_month);
        charts_change_over_time_2.setText(R.string.over_time_one_year);
    }

    public void createChart(){
        //Set Chart Entries
        List<Entry> entries = new ArrayList<Entry>();
        startDate = mDataSet.get(0).getDate();
        for(int i = 0; i < mDataSet.size(); i++){
            entries.add(i, new Entry(i * .1f, mDataSet.get(i).getWeight()));
        }
        LineDataSet entrySet = new LineDataSet(entries, null); // add entries to dataset

        //entriesSet options
        entrySet.setDrawValues(false);
        entrySet.setDrawFilled(true);
        if(mDataSet.size() != 1)
            entrySet.setDrawCircles(false);
        else
            entrySet.setCircleHoleRadius(10);

        LineData lineData = new LineData(entrySet);
        //chart options
        //Disable Chart Interactions
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        //Axis options
        YAxis leftAxis;
        leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(.5f);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        xAxis.setGranularity(TimeConversions.ONE_DAY_FLOAT);

        chart.setData(lineData);
        chartMaxSize = chart.getHighestVisibleX();
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.colorChartBorder));
        chart.setBorderWidth(1);
        chart.setExtraOffsets(2, 0, 20, 0);
        chart.setAutoScaleMinMaxEnabled(true);
        chartFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChartFullScreen.class);
                Gson gson = new Gson();
                String dataSetString = gson.toJson(mDataSet);
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
        expandButton.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);
        buttonBar.setVisibility(View.VISIBLE);
    }

    public void dataRetrievalFailure(String message){

        String failedText = getString(R.string.charts_api_failed_fields);
        charts_current_weight.setText(failedText);
        charts_high_response.setText(failedText);
        charts_low_response.setText(failedText);
        charts_year_high_response.setText(failedText);
        charts_year_low_response.setText(failedText);
        charts_change_over_time_1_response.setText(failedText);
        charts_change_over_time_2_response.setText(failedText);

        chart.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.GONE);
        charts_failed_message.setVisibility(View.VISIBLE);
        messageText.setText(message);
        chartFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightEntries(baseActivityListener.getTokenPref().getString(getString(R.string.token_JWT_preference), null));
            }
        });
    }

    public void getWeightEntries(String token){

        if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message))) {
            Intent intent = new Intent(getActivity(), Main.class);
            Utils.refreshToken(baseActivityListener.getTokenPref(), getActivity(), intent);
            Call<List<WeightEntry>> call = baseActivityListener.getApiInterface().getWeightData(token);
            call.enqueue(new Callback<List<WeightEntry>>() {
                @Override
                public void onResponse(Call<List<WeightEntry>> call, Response<List<WeightEntry>> response) {
                    if (response.isSuccessful()) {
                        pBar.setVisibility(View.VISIBLE);
                        charts_failed_message.setVisibility(View.GONE);
                        mDataSet = response.body();
                        baseActivityListener.setDataSetCharts(mDataSet);
                        dataSetReceived();
                    }
                }

                @Override
                public void onFailure(Call<List<WeightEntry>> call, Throwable t) {
                    call.cancel();
                    dataRetrievalFailure(getString(R.string.charts_api_failed_message_part1));
                }
            });
        }
        else{
            dataRetrievalFailure(getString(R.string.no_connection_message));
        }
    }

    public void dataSetReceived() {
        if (mDataSet.size() <= 1) {
            mDataSet = ExampleData.getFakedData();
            setFakeChartStatistics();
            createChart();
            setInitialViewport();
            removeLoadingScreen();
            addNoDataCover();
        } else {
            setChartStatistics();
            createChart();
            setInitialViewport();
            removeLoadingScreen();
        }
    }

    public void addNoDataCover(){
        Blurry.with(getActivity())
                .radius(25)
                .sampling(1)
                .capture(mainView)
                .into(noDataImage);
        mainView.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
        noDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivityListener.swapFragment(R.id.action_create, true);
            }
        });
    }
}

