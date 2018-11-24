package com.example.drcir.weighttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class enterWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_weight);

        CalendarView calendar = findViewById(R.id.calendarView);
        TextView date = findViewById(R.id.dateEntry);
        date.setText(String.valueOf(calendar.getDate()));
    }
}
