package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cleverpumpkin.calendar.CalendarDate;
import ru.cleverpumpkin.calendar.CalendarView;

public class EnterWeightFragment extends Fragment {

    private BaseActivityListener baseActivityListener;
    Long selectedDate;
    int enteredWeight;

    TextView selectedDateView;
    EditText enteredWeightView;
    Button submit;
    CalendarView calendarView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter_weight, container, false);
        enteredWeightView = rootView.findViewById(R.id.enteredWeight);
        submit = rootView.findViewById(R.id.submit);
        calendarView = rootView.findViewById(R.id.calendarView);
        selectedDateView = rootView.findViewById(R.id.selectedDate);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        CalendarDate initialDate = new CalendarDate(calendar.getTime());
        //Minium date set to 01/01/2008
        CalendarDate minDate = new CalendarDate(1199188800000L);
        int firstDayOfWeek = Calendar.SUNDAY;
        List<CalendarDate> preselectedDates = new ArrayList<>();
        preselectedDates.add(initialDate);
        calendarView.setupCalendar(initialDate, minDate, initialDate, CalendarView.SelectionMode.SINGLE, preselectedDates, firstDayOfWeek, false);
        selectedDate = initialDate.getTimeInMillis();
        selectedDateView.setText(Utils.formatSelectedDate(selectedDate));

        enteredWeightView.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        enteredWeightView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(enteredWeightView.getText().toString().trim().length()!= 0)
                        submit.performClick();
                }
                return false;
            }
        });

        enteredWeightView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(enteredWeightView.getText().toString().trim().length()==0){
                    submit.setEnabled(false);
                } else {
                    submit.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        calendarView.setOnDateClickListener(new Function1<CalendarDate, Unit>() {
            @Override
            public Unit invoke(CalendarDate userSelectedDate) {
                selectedDate = userSelectedDate.getTimeInMillis();
                selectedDateView.setText(Utils.formatSelectedDate(selectedDate));
                return null;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredWeight = Integer.parseInt(enteredWeightView.getText().toString());
                if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message_create)) && verifyWeight()){
                        baseActivityListener.getCurrentUser().getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        String token = task.getResult().getToken();
                                        Date date = new Date(selectedDate);
                                        String modifiedDate= new SimpleDateFormat("MM/dd/yyyy").format(date);
                                        Call<Void> call = baseActivityListener.getApiInterface().createWeight(token, modifiedDate, enteredWeight);
                                        call.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    baseActivityListener.setUpdateDataSets(true);
                                                    baseActivityListener.swapFragment(R.id.action_charts);
                                                    Toast.makeText(getActivity(), getString(R.string.enter_weight_created), Toast.LENGTH_LONG).show();
                                                }
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
                                    else{
                                        Intent intent = new Intent(getActivity(), Main.class);
                                        Utils.logout(getActivity(), intent);
                                    }
                                }
                            });
                }
                enteredWeightView.setText(null);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    public void createFailed(){
        Toast.makeText(getActivity(), getString(R.string.enter_weight_failed), Toast.LENGTH_LONG).show();
    }

    public void createInvalid(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public boolean verifyWeight(){
        boolean verified = true;
        if(enteredWeight < 0 || enteredWeight >= 1000) {
            createInvalid(getString(R.string.enter_weight_invalid));
            verified = false;
        }
        return verified;
    }
}
