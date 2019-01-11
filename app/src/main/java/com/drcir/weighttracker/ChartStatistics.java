package com.drcir.weighttracker;

import java.util.List;

public class ChartStatistics {
    private int high1y;
    private int low1y;
    private int highMax;
    private int lowMax;
    private int change6mo;
    private int change1y;
    int currentWeight;//

    public ChartStatistics(List<WeightEntry> dataset){
        float year = TimeConversions.ONE_YEAR_MILLI;
        float sixMonths = TimeConversions.SIX_MONTHS_MILLI;
        float currentTime = System.currentTimeMillis();
        int sixMonthIndex;
        int yearIndex;
        currentWeight = dataset.get(dataset.size()-1).getWeight();

        //Entries greater than 6 months
        if(currentTime - dataset.get(0).getDate() > sixMonths){
            sixMonthIndex = getTimeIndex(sixMonths, dataset, dataset.size()-1);
            change6mo = getWeightChange(sixMonthIndex, sixMonths, dataset);
            //Entries greater than a year
            if(currentTime - dataset.get(0).getDate() > year){
                //Set 1 year High/low
                yearIndex = getTimeIndex(year, dataset, dataset.size()-1);
                int highLowYear[] = getHighLowWeight(yearIndex, dataset.size()-1,dataset);
                high1y = highLowYear[0];
                low1y = highLowYear[1];
                change1y = getWeightChange(yearIndex, year, dataset);
                //Set alltime high/low
                int highLowAll[] = getHighLowWeight(0, yearIndex-1 ,dataset);
                if(highLowAll[0] > high1y)
                    highMax = highLowAll[0];
                else
                    highMax = high1y;
                if(highLowAll[1] < low1y)
                    lowMax = highLowAll[1];
                else
                    lowMax = low1y;
            }
            //Entries between 6mo and 1 year
            else{
                change1y = change6mo;
                int highLowAll[] = getHighLowWeight(0, dataset.size()-1 ,dataset);
                high1y = highMax =  highLowAll[0];
                low1y = lowMax = highLowAll[1];
            }
        }
        //Entries less than 6 months
        else{
            int highLowAll[] = getHighLowWeight(0, dataset.size()-1 ,dataset);
            high1y = highMax =  highLowAll[0];
            low1y = lowMax = highLowAll[1];
            change6mo = highMax - currentWeight;
            change1y = change6mo;
        }
    }

    private int[] getHighLowWeight(int startIndex, int endIndex, List<WeightEntry> entries){
        int[] highlow = {entries.get(startIndex).getWeight(), entries.get(startIndex).getWeight()};
        int temp;
        for(int i = startIndex + 1; i <= endIndex; i++){
            temp = entries.get(i).getWeight();
            if(temp > highlow[0])
                highlow[0] = temp;
            if(temp < highlow[1])
                highlow[1] = temp;
        }

        return highlow;
    }



    //timePeriod = Six months in milliseconds
    //startIndex = Index of first date prior to time period.
    //difference =

//now - six months

    private int getWeightChange (int startIndex, float timePeriod, List<WeightEntry> dataset){
        int difference = 0;
        float oneDayMilliseconds = TimeConversions.ONE_DAY_MILLI;
        float dateNow =System.currentTimeMillis();
        float dateStartIndex = dataset.get(startIndex).getDate();
        //If multiple entries and not the same day
        if(startIndex != dataset.size()-1 && !(dateStartIndex/oneDayMilliseconds == timePeriod/oneDayMilliseconds)){
            float nextEntryDate = dataset.get(startIndex + 1).getDate();
            float millisCurrentIndexOffGoal = (dateNow - timePeriod) - dateStartIndex;
            float millsCurrentIndexNextIndex = nextEntryDate - dateStartIndex;
            int weightDiff = dataset.get(startIndex + 1).getWeight() - dataset.get(startIndex).getWeight();
            float daysCurrentIndexOffGoal = (float) millisCurrentIndexOffGoal/oneDayMilliseconds;
            float daysCurrentIndexNextIndex = (float)millsCurrentIndexNextIndex/oneDayMilliseconds;
            //Average out difference for periods of no entry
            int previousWeight = dataset.get(startIndex).getWeight() + (int)(daysCurrentIndexNextIndex/daysCurrentIndexOffGoal*weightDiff);
            difference = currentWeight - previousWeight;
        }
        return difference;
    }

    private int getTimeIndex(float timePeriod, List<WeightEntry> entries, int startIndex){
        float time = System.currentTimeMillis() - timePeriod;
        int index = 0;
        for(int i = startIndex; i >= 0 && index == 0; i--){
            if(entries.get(i).getDate() < time)
                index = i;
        }
        return index;
    }

    public int getHigh1y() {
        return high1y;
    }

    public void setHigh1y(int high1y) {
        this.high1y = high1y;
    }

    public int getLow1y() {
        return low1y;
    }

    public void setLow1y(int low1y) {
        this.low1y = low1y;
    }

    public int getHighMax() {
        return highMax;
    }

    public void setHighMax(int highMax) {
        this.highMax = highMax;
    }

    public int getLowMax() {
        return lowMax;
    }

    public void setLowMax(int lowMax) {
        this.lowMax = lowMax;
    }

    public int getChange6mo() {
        return change6mo;
    }

    public void setChange6mo(int change6mo) {
        this.change6mo = change6mo;
    }

    public int getChange1y() {
        return change1y;
    }

    public void setChange1y(int change1y) {
        this.change1y = change1y;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

}
