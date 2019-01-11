package com.drcir.weighttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        holder.mWeight.setText(mDataset.get(position).getWeight().toString());
        holder.mEnteredDate.setText(Utils.formatDate(mDataset.get(position).getDateEntered()));
        holder.mActive.setChecked(mDataset.get(position).getActive());
    }

    @Override
    public int getItemCount() {
        return (null != mDataset ? mDataset.size() : 0);
    }

}
