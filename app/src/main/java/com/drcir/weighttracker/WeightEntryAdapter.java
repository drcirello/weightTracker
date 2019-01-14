package com.drcir.weighttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WeightEntryAdapter extends RecyclerView.Adapter<WeightEntryAdapter.WeightEntryViewHolder> {

    private List<WeightEntry> mDataset;

    public static class WeightEntryViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private TextView mWeight;
        private TextView mEnteredDate;
        private Switch mActive;

        public WeightEntryViewHolder(final View v) {
            super(v);
            this.mDate = (TextView) v.findViewById(R.id.entries_date_response);
            this.mWeight = (TextView) v.findViewById(R.id.entries_weight_response);
            this.mEnteredDate = (TextView) v.findViewById(R.id.entries_date_entered_response);
            this.mActive = (Switch) v.findViewById(R.id.entries_active_entry);

        }
    }

    public WeightEntryAdapter(List<WeightEntry> weightEntries) {
        Collections.reverse(weightEntries);
        mDataset = weightEntries;
    }

    @Override
    public WeightEntryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_entry_item, parent, false);
        return new WeightEntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WeightEntryViewHolder holder, int position) {
        holder.mDate.setText(Utils.formatDate(mDataset.get(position).getDate()));
        holder.mWeight.setText(Float.toString(mDataset.get(position).getWeight()));
        if(mDataset.get(position).getDateEntered() != 0)
            holder.mEnteredDate.setText(Utils.formatDate(mDataset.get(position).getDateEntered()));
        else
            holder.mEnteredDate.setText(Utils.formatDate(mDataset.get(position).getDate()));
        if(mDataset.get(position).getActive() != null)
            holder.mActive.setChecked(mDataset.get(position).getActive());
        else
            holder.mActive.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }

}
