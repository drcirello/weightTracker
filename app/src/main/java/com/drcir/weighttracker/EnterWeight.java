package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cleverpumpkin.calendar.CalendarDate;
import ru.cleverpumpkin.calendar.CalendarView;


public class EnterWeight extends AppCompatActivity {

    Long selectedDate;
    TextView selectedDateView;
    float enteredWeight;
    EditText enteredWeightView;

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

        enteredWeightView = findViewById(R.id.enteredWeight);

        //setup calendar
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
                SharedPreferences sharedPrefToken = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
                String token = sharedPrefToken.getString(getString(R.string.token_JWT_preference), null);
                APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

                Date date = new Date(selectedDate);
                String modifiedDate= new SimpleDateFormat("MM/dd/yyyy").format(date);
                if(verifyWeight(enteredWeightView.getText().toString())){
                    Call<Void> call = apiInterface.createWeight(token, modifiedDate, enteredWeight);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful())
                                Toast.makeText(getApplicationContext(), getString(R.string.enter_weight_created), Toast.LENGTH_LONG).show();
                            else
                                createFailed();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            createFailed();
                            call.cancel();
                        }
                    });
                }
                else {
                    createInvalid();
                }
                enteredWeightView.setText(null);
            }
        });
    }

    public void createFailed(){
        Toast.makeText(getApplicationContext(), getString(R.string.enter_weight_failed), Toast.LENGTH_LONG).show();
    }

    public void createInvalid(){
        Toast.makeText(getApplicationContext(), getString(R.string.enter_weight_invalid), Toast.LENGTH_LONG).show();
    }

    public boolean verifyWeight(String entry){
        boolean verified = true;
        try {
            DecimalFormat form = Utils.getDecimalFormat();
            String preciseWeight = form.format(Float.parseFloat(entry));
            enteredWeight = Float.parseFloat(preciseWeight);
            if(enteredWeight < 0 || enteredWeight >= 1000)
                verified = false;
        }
        catch (Exception e){
            verified = false;
        }

        return verified;
    }

}
