package com.drcir.weighttracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightEntryAdapter extends RecyclerView.Adapter<WeightEntryAdapter.WeightEntryViewHolder> {

    private List<WeightEntry> mDataSet;
    private BaseActivityListener baseActivityListener;;

    public static class WeightEntryViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private TextView mWeight;
        private Button mDelete;

        public WeightEntryViewHolder(final View v) {
            super(v);
            this.mDate = v.findViewById(R.id.entries_date_response);
            this.mWeight = v.findViewById(R.id.entries_weight_response);
            this.mDelete = v.findViewById(R.id.entries_delete_entry);
        }
    }

    public WeightEntryAdapter(List<WeightEntry> weightEntries, Context context) {
        int size = weightEntries.size();
        if(size > 1){
            if(weightEntries.get(0).getDate() < weightEntries.get(size-1).getDate())
                Collections.reverse(weightEntries);
        }
        mDataSet = weightEntries;
        baseActivityListener = (BaseActivityListener) context;
    }

    @Override
    public WeightEntryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_entry_item, parent, false);
        return new WeightEntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WeightEntryViewHolder holder, int position) {
            final View viewThemeContext = holder.itemView;
            holder.mDate.setText(Utils.formatDate(mDataSet.get(position).getDate()));
            holder.mWeight.setText(Integer.toString(Math.round(mDataSet.get(position).getWeight())));
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(Utils.checkConnection(viewThemeContext.getContext(), viewThemeContext.getContext().getString(R.string.no_connection_message_delete))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(viewThemeContext.getContext());
                        builder.setTitle("Delete entry")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int currentPosition = holder.getAdapterPosition();
                                        removeEntry(viewThemeContext.getContext(), currentPosition);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return (null != mDataSet ? mDataSet.size() : 0);
    }

    public void removeEntry(final Context context, final int position){
        if(Utils.checkConnection(context, context.getString(R.string.no_connection_message))) {
            baseActivityListener.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            int weightId = mDataSet.get(position).getWeightId();
                            Call<Void> call = baseActivityListener.getApiInterface().deleteWeight(token, weightId);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()) {
                                        mDataSet.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mDataSet.size());
                                        baseActivityListener.setUpdateDataSets(true);
                                        Toast.makeText(context, "Entry Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t)
                                {
                                    Toast.makeText(context, "Error Deleting Entry", Toast.LENGTH_LONG).show();
                                    call.cancel();
                                }
                            });
                        }
                    }
                });
        }
    }

}
