package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightEntriesFragment extends Fragment {

    private BaseActivityListener baseActivityListener;
    List<WeightEntry> mDataSet;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RelativeLayout emptyView;
    RelativeLayout entriesFrame;
    LinearLayout entriesFailedMessage;
    ProgressBar pBar;
    RecyclerView.LayoutManager mLayoutManager;

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
        View rootView = inflater.inflate(R.layout.fragment_weight_entries_recycler, container, false);

        //Setup recycler
        mRecyclerView = rootView.findViewById(R.id.weight_entries_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.recycler_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        entriesFailedMessage = rootView.findViewById(R.id.failedMessage);
        pBar = rootView.findViewById(R.id.pBar);
        entriesFrame = rootView.findViewById(R.id.entriesFrame);
        emptyView = rootView.findViewById(R.id.empty_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        if(baseActivityListener.getUpdateDataSetEntries()) {
            if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message))) {
                baseActivityListener.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String token = task.getResult().getToken();
                                getWeightEntries(token);
                            }
                            else{
                                Intent intent = new Intent(getActivity(), Main.class);
                                Utils.logout(getActivity(), intent);
                            }
                        }
                    });
            }
            else {
                setNoConnection();
            }
        }
        else {
            mDataSet = baseActivityListener.getDataSetEntries();
            dataSetReceived();
        }
        super.onResume();
    }

    public void dataRetrievalFailure(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.GONE);
        entriesFailedMessage.setVisibility(View.VISIBLE);
        entriesFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message))) {
                baseActivityListener.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String token = task.getResult().getToken();
                                getWeightEntries(token);
                            }
                            else{
                                Intent intent = new Intent(getActivity(), Main.class);
                                Utils.logout(getActivity(), intent);
                            }
                        }
                    });
            }
            else {
                setNoConnection();
            }
            entriesFrame.setOnClickListener(null);
            }
        });
    }

    public void setNoConnection(){
        mRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.GONE);
        TextView noDataLineOne = getView().findViewById(R.id.no_data_available_line_one);
        TextView noDataLineTwo = getView().findViewById(R.id.no_data_available_line_two);
        noDataLineOne.setText(R.string.no_connection_message);
        noDataLineTwo.setText("");
    }

    public void getWeightEntries(String token){
        pBar.setVisibility(View.VISIBLE);
        entriesFailedMessage.setVisibility(View.GONE);
        if(Utils.checkConnection(WeightEntriesFragment.this.getActivity(), getString(R.string.no_connection_message))) {
            Call<List<WeightEntry>> call = baseActivityListener.getApiInterface().getWeightEntries(token);
            call.enqueue(new Callback<List<WeightEntry>>() {
                @Override
                public void onResponse(Call<List<WeightEntry>> call, Response<List<WeightEntry>> response) {
                    if (response.isSuccessful()) {
                        mDataSet = response.body();
                        baseActivityListener.setDataSetEntries(mDataSet);
                        dataSetReceived();
                    }
                }

                @Override
                public void onFailure(Call<List<WeightEntry>> call, Throwable t) {
                    dataRetrievalFailure();
                    call.cancel();
                }
            });
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.GONE);
            TextView noDataLineOne = getView().findViewById(R.id.no_data_available_line_one);
            TextView noDataLineTwo = getView().findViewById(R.id.no_data_available_line_two);
            noDataLineOne.setText(R.string.no_connection_message);
            noDataLineTwo.setText("");
        }
    }

    public void dataSetReceived() {
        mAdapter = new WeightEntryAdapter(mDataSet, getContext());
        mRecyclerView.swapAdapter(mAdapter, false);

        if (mDataSet.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        pBar.setVisibility(View.GONE);
        entriesFailedMessage.setVisibility(View.GONE);
    }
}





