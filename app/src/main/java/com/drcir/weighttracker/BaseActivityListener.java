package com.drcir.weighttracker;

import android.content.SharedPreferences;

import java.util.List;

public interface BaseActivityListener {
    public void swapFragment(int fragment, boolean addToStack);

    public boolean getUpdateDataSetCharts();
    public List<WeightEntry> getDataSetCharts();
    public void setDataSetCharts(List<WeightEntry> dataSet);

    public boolean getUpdateDataSetEntries();
    public List<WeightEntry> getDataSetEntries();
    public void setDataSetEntries(List<WeightEntry> dataSet);

    public void setUpdateDataSets(boolean update);

    public SharedPreferences getTokenPref();
    public SharedPreferences getRangePref();

    public APIInterface getApiInterface();
}