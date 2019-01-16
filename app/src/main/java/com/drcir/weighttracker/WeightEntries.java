package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightEntries extends AppCompatActivity {

    static List<WeightEntry> dataSet;
    String token;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    RelativeLayout entriesFrame;
    LinearLayout entriesFailedMessage;
    ProgressBar pBar;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        entriesFailedMessage = findViewById(R.id.failedMessage);
        pBar = findViewById(R.id.pBar);
        entriesFrame = (RelativeLayout) findViewById(R.id.entriesFrame);

        mRecyclerView = (RecyclerView) findViewById(R.id.weight_entries_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.recycler_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        SharedPreferences sharedPrefToken = getSharedPreferences(getString(R.string.token_preferences), Context.MODE_PRIVATE);
        token = sharedPrefToken.getString(getString(R.string.token_JWT_preference), null);
        getWeightEntries(token);

    }

    public void dataRetrievalFailure(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.GONE);
        entriesFailedMessage.setVisibility(View.VISIBLE);
        entriesFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightEntries(token);
                entriesFrame.setOnClickListener(null);
            }
        });
    }

    public void getWeightEntries(String token){
        pBar.setVisibility(View.VISIBLE);
        entriesFailedMessage.setVisibility(View.GONE);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<WeightEntry>> call = apiInterface.getWeightEntries(token);
        call.enqueue(new Callback<List<WeightEntry>>() {
            @Override
            public void onResponse(Call<List<WeightEntry>> call, Response<List<WeightEntry>> response) {
                if(response.isSuccessful()) {
                    dataSet = response.body();
                    mAdapter = new WeightEntryAdapter (dataSet);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    pBar.setVisibility(View.GONE);
                    entriesFailedMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<WeightEntry>> call, Throwable t)
            {
                dataRetrievalFailure();
                call.cancel();
            }
        });
    };
}
