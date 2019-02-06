package com.drcir.weighttracker;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface BaseActivityListener {
    public void swapFragment(int fragment);

    public boolean getUpdateDataSetCharts();
    public List<WeightEntry> getDataSetCharts();
    public void setDataSetCharts(List<WeightEntry> dataSet);

    public boolean getUpdateDataSetEntries();
    public List<WeightEntry> getDataSetEntries();
    public void setDataSetEntries(List<WeightEntry> dataSet);

    public void setUpdateDataSets(boolean update);

    public SharedPreferences getRangePref();

    public APIInterface getApiInterface();

    public Drawable getNoDataImage();
    public void setNoDataImage(Drawable image);

    public FirebaseUser getCurrentUser();
}