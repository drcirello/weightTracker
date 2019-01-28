package com.drcir.weighttracker;

import com.drcir.weighttracker.data.DataDefinitions;

import java.util.Calendar;
import java.util.List;

public class ChartStatistics {
    private float high1y;
    private float low1y;
    private float highMax;
    private float lowMax;
    private float changeOverTime1;
    private float changeOverTime2;
    float currentWeight;//

    public ChartStatistics(List<WeightEntry> dataset, int changeOverTimePref1, int changeOverTimePref2){
        currentWeight = dataset.get(dataset.size()-1).getWeight();
        changeOverTime1 = getChangeOverTime(changeOverTimePref1, dataset);
        changeOverTime2 = getChangeOverTime(changeOverTimePref2, dataset);
        getHighLowWeight(dataset);
    }

    private void getHighLowWeight(List<WeightEntry> dataset){
        int dataSize = dataset.size();
        float temp;

        highMax = dataset.get(dataSize - 1).getWeight();
        lowMax = dataset.get(dataSize - 1).getWeight();
        for(int i = 1; i <= dataSize; i++){
            temp = dataset.get(dataSize - i).getWeight();
            if(temp > highMax)
                highMax = temp;
            if(temp < lowMax)
                lowMax = temp;
            if(i == DataDefinitions.ONE_YEAR){
                high1y = highMax;
                low1y = lowMax;
            }
        }
        if(dataset.size() < DataDefinitions.ONE_YEAR){
            high1y = highMax;
            low1y = lowMax;
        }
    }

    private float getChangeOverTime (int timePeriod, List<WeightEntry> dataset){
        float difference;
        int dataSize = dataset.size();
        //YTD
        if(timePeriod == -1) {
            Calendar cal = Calendar.getInstance();
            timePeriod = cal.get(Calendar.DAY_OF_YEAR);
        }
        //Max
        if(timePeriod == 0 || timePeriod > dataSize)
            difference = currentWeight - dataset.get(0).getWeight();
        else
            difference = currentWeight - dataset.get(dataSize - 1 - timePeriod).getWeight();

        return difference;
    }

    public float getHigh1y() {
        return high1y;
    }
    public float getLow1y() {
        return low1y;
    }
    public float getHighMax() {
        return highMax;
    }
    public float getLowMax() {
        return lowMax;
    }
    public float getChangeOverTime1() {
        return changeOverTime1;
    }
    public float getChangeOverTime2() {
        return changeOverTime2;
    }
    public float getCurrentWeight() {
        return currentWeight;
    }

}
