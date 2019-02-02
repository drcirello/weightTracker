package com.drcir.weighttracker;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class CustomFillFormatter implements IFillFormatter {

    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
        float myDesiredFillPosition = 0f;


        float chartMaxY = dataProvider.getYChartMax();
        float chartMinY = dataProvider.getYChartMin();

        LineData data = dataProvider.getLineData();

        if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            myDesiredFillPosition = 0f;
        } else {

            float max, min;

            if (data.getYMax() > 0)
                max = 0f;
            else
                max = chartMaxY;
            min = 0f;

            myDesiredFillPosition = dataSet.getYMin() >= 0 ? min : max;
        }
        return myDesiredFillPosition;
    }
}