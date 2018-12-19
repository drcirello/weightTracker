package com.drcir.weighttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import android.widget.CalendarView;
import android.widget.TextView;

public class EnterWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_weight);


        /* Chart Stuff
        List<Entry> entries = new ArrayList<Entry>();

        entries.add(0, new Entry(0, 185));
        entries.add(1, new Entry(1, 190));
        entries.add(2, new Entry(2, 188));
        entries.add(3, new Entry(3, 180));


        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        //dataSet.setColor(...);
        //dataSet.setValueTextColor(...);

        LineData lineData = new LineData(dataSet);
        LineChart lc = findViewById(R.id.lineChart);
        lc.setData(lineData);
        //lc.invalidate();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 30f));
        barEntries.add(new BarEntry(1f, 80f));
        barEntries.add(new BarEntry(2f, 60f));
        barEntries.add(new BarEntry(3f, 50f));
        barEntries.add(new BarEntry(5f, 70f));
        barEntries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(barEntries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        BarChart bc = findViewById(R.id.statsChart);
        bc.setData(data);
        bc.setFitBars(true); // make the x-axis fit exactly all bars
        //bc.invalidate(); // refresh

        Button b = findViewById(R.id.enterWeight);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentApp = new Intent(Main.this,
                        EnterWeight.class);
                Main.this.startActivity(intentApp);
            }
        });
        */

    }
}
