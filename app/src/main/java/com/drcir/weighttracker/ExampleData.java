package com.drcir.weighttracker;

import java.util.ArrayList;
import java.util.List;

public class ExampleData {

    public static List<WeightEntry> getFakedData() {
        List<WeightEntry> dataSet = new ArrayList<>();
        dataSet.add(new WeightEntry(1, 1700));
        dataSet.add(new WeightEntry(2, 1750));
        dataSet.add(new WeightEntry(3, 1720));
        dataSet.add(new WeightEntry(4, 1730));
        dataSet.add(new WeightEntry(5, 1760));
        dataSet.add(new WeightEntry(6, 1770));
        dataSet.add(new WeightEntry(7, 1800));
        dataSet.add(new WeightEntry(8, 1900));
        dataSet.add(new WeightEntry(9, 1810));
        dataSet.add(new WeightEntry(10, 1840));
        dataSet.add(new WeightEntry(11, 1810));
        dataSet.add(new WeightEntry(12, 1820));

        return dataSet;
    }
}
