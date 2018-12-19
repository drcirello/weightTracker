package com.drcir.weighttracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightEntry {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("dateEntered")
    @Expose
    private String dateEntered;

    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}