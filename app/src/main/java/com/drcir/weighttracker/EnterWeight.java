package com.drcir.weighttracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import ru.cleverpumpkin.calendar.CalendarDate;
import ru.cleverpumpkin.calendar.CalendarView;


public class EnterWeight extends AppCompatActivity {

    Long selectedDate;
    TextView selectedDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_weight);
        Toolbar myTitlebar = findViewById(R.id.titleBar);
        setSupportActionBar(myTitlebar);
        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_create);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        Intent intent;
                        switch (id) {
                            case R.id.action_charts:
                                intent = new Intent(EnterWeight.this, Charts.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_view:
                                intent = new Intent(EnterWeight.this, WeightEntries.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(EnterWeight.this, Settings.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_create:
                            default:
                                break;
                        }
                        return false;
                    }
                });

        final CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        CalendarDate initialDate = new CalendarDate(calendar.getTime());
        int firstDayOfWeek = Calendar.SUNDAY;
        List<CalendarDate> preselectedDates = new ArrayList<>();
        preselectedDates.add(initialDate);
        calendarView.setupCalendar(initialDate, null, null, CalendarView.SelectionMode.SINGLE, preselectedDates, firstDayOfWeek, false);

        selectedDate = initialDate.getTimeInMillis();
        selectedDateView = findViewById(R.id.selectedDate);
        selectedDateView.setText(Utils.formatSelectedDate(selectedDate));

        calendarView.setOnDateClickListener(new Function1<CalendarDate, Unit>() {
            @Override
            public Unit invoke(CalendarDate userSelectedDate) {
                selectedDate = userSelectedDate.getTimeInMillis();
                selectedDateView.setText(Utils.formatSelectedDate(selectedDate));
                return null;
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnterWeight.this, "Submit!",
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
