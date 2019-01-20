package com.drcir.weighttracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightEntry {

    public WeightEntry(long date, Integer weight){
        this.setDate(date);
        this.setWeight(weight);
    }

    @SerializedName("date")
    @Expose
    private long date;

    @SerializedName("weight_times_10")
    @Expose
    private Integer weight;

    @SerializedName("weight_id")
    @Expose
    private int weightId;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getWeight() {
        return weight / (float)10.0;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public int getWeightId() {
        return weightId;
    }

    public void setWeightId(int weightId) {
        this.weightId = weightId;
    }
}