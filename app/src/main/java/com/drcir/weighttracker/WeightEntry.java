package com.drcir.weighttracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightEntry {

    @SerializedName("date")
    @Expose
    private long date;

    @SerializedName("weight_times_10")
    @Expose
    private Integer weight;

    @SerializedName("dateEntered")
    @Expose
    private long dateEntered;

    @SerializedName("active")
    @Expose
    private Boolean active;

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

    public long getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(long dateEntered) {
        this.dateEntered = dateEntered;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}