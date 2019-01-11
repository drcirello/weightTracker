package com.drcir.weighttracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        setContentView(R.layout.weight_entries_recycler);
        Toolbar myTitlebar = (Toolbar) findViewById(R.id.titleBar);
        setSupportActionBar(myTitlebar);
        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.action_view);
        navBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();

                        Intent intent;
                        switch (id) {
                            case R.id.action_charts:
                                intent = new Intent(WeightEntries.this, Charts.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_create:
                                intent = new Intent(WeightEntries.this, EnterWeight.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(WeightEntries.this, Settings.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_view:
                            default:
                                break;
                        }
                        return false;
                    }
                });


        mRecyclerView = (RecyclerView) findViewById(R.id.weight_entries_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.recycler_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        dataset = TestData.getTestData();
        //getWeightEntries();

        mAdapter = new WeightEntryAdapter (dataset);
        mRecyclerView.setAdapter(mAdapter);


    }

    /*
    public void getWeightEntries(){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sharedPref.getString(getString(R.string.token), null);
        Call<WeightEntry> call = apiInterface.getWeightData(token);
        call.enqueue(new Callback<WeightEntry>() {
            @Override
            public void onResponse(Call<WeightEntry> call, Response<WeightEntry> response) {
                if(response.isSuccessful()) {
                    response.body();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<WeightEntry>>(){}.getType();
                    //List<WeightEntry> we = gson.fromJson(response.body(), listType);
                    //dataset = we;
                }
            }

            @Override
            public void onFailure(Call<WeightEntry> call, Throwable t) {
                call.cancel();
            }
        });
    };
    */
}
