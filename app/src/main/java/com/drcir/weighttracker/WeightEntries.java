package com.drcir.weighttracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightEntries extends AppCompatActivity {

    List<WeightEntry> dataset;
    APIInterface apiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_entries_recycler);
        Toolbar myTitlebar = (Toolbar) findViewById(R.id.titleBar);
        setSupportActionBar(myTitlebar);
        mRecyclerView = (RecyclerView) findViewById(R.id.weight_entries_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.recycler_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        dataset = getTestData();
        //getWeightEntries();

        mAdapter = new WeightEntryAdapter (dataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getWeightEntries(){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sharedPref.getString(getString(R.string.token), null);
        Call<WeightEntry> call = apiInterface.getWeightData(token);
        call.enqueue(new Callback<WeightEntry>() {
            @Override
            public void onResponse(Call<WeightEntry> call, Response<WeightEntry> response) {
                if(response.isSuccessful()) {
                    //dataset = response.body();
                }
            }

            @Override
            public void onFailure(Call<WeightEntry> call, Throwable t) {
                call.cancel();
            }
        });
    };

    public List<WeightEntry> getTestData(){
        Gson gson = new Gson();
        String test = "[\n" +
                "{\n" +
                "\"date\": \"September 15, 2021\",\n" +
                "\"weight\": 200,\n" +
                "\"dateEntered\": \"September 15, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"September 16, 2021\",\n" +
                "\"weight\": 204,\n" +
                "\"dateEntered\": \"September 16, 2021\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"September 17, 2021\",\n" +
                "\"weight\": 204,\n" +
                "\"dateEntered\": \"September 17, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"May 1, 2021\",\n" +
                "\"weight\": 193,\n" +
                "\"dateEntered\": \"September 15, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"September 1, 2021\",\n" +
                "\"weight\": 203,\n" +
                "\"dateEntered\": \"May 5, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"March 2, 2021\",\n" +
                "\"weight\": 200,\n" +
                "\"dateEntered\": \"March 3, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"April 1, 2021\",\n" +
                "\"weight\": 205,\n" +
                "\"dateEntered\": \"September 15, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"January 15, 2020\",\n" +
                "\"weight\": 199,\n" +
                "\"dateEntered\": \"September 15, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"June 2, 2021\",\n" +
                "\"weight\": 196,\n" +
                "\"dateEntered\": \"July 1, 2021\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"July 4, 2021\",\n" +
                "\"weight\": 195,\n" +
                "\"dateEntered\": \"December 25, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"August 2, 2021\",\n" +
                "\"weight\": 198,\n" +
                "\"dateEntered\": \"June 10, 2021\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"February 1, 2021\",\n" +
                "\"weight\": 210,\n" +
                "\"dateEntered\": \"November 15, 2021\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"October 15, 2021\",\n" +
                "\"weight\": 206,\n" +
                "\"dateEntered\": \"May 30, 2021\",\n" +
                "\"active\": true\n" +
                "}\n" +
                "]\n";

        Type listType = new TypeToken<List<WeightEntry>>(){}.getType();
        List<WeightEntry> we = gson.fromJson(test, listType);
        return we;
    }


}
