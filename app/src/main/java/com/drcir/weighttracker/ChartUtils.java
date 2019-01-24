package com.drcir.weighttracker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChartUtils {

    public static XAxis setXaxisScale(XAxis xAxis, float scale, long datasetLength){
        if(datasetLength < scale)
            scale = datasetLength;
        if(scale <= TimeConversions.THREE_MONTHS_MILLI) {
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterShort());
        }
        else if(scale < TimeConversions.ONE_YEAR_MILLI) {
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterMedium());
        }
        else{
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterLong());
        }
        xAxis.setLabelCount(XaxisLabels(scale), true);
        return xAxis;
    }

    public static LineChart updateChartViewport(LineChart chart, float scale){
        //resets entire chart
        chart.fitScreen();
        //gets the maximum possible x value
        ViewPortHandler handler = chart.getViewPortHandler();
        float maxScale = handler.getMaxScaleX();
        //Sets viewport size for selected scale
        chart.setVisibleXRangeMaximum(scale);
        //Moves viewport back to current position
        chart.moveViewToX(maxScale - scale);
        return chart;
    }

    public static LineChart updateChartViewportFullscreen(LineChart chart, float scale, float chartMinValue){
        //get current right side of viewport
        float highViewX = chart.getHighestVisibleX();
        //resets entire chart
        chart.fitScreen();
        //Sets viewport size for selected scale
        chart.setVisibleXRangeMaximum(scale);
        if(highViewX - scale < chartMinValue)
            chart.moveViewToX(chartMinValue);
        else
            chart.moveViewToX(highViewX - scale);
        //gets the maximum possible x value
        ViewPortHandler handler = chart.getViewPortHandler();
        float maxScale = handler.getMaxScaleX();
        //Allows pinch zooming to full range
        chart.setVisibleXRangeMaximum(maxScale);
        return chart;
    }

    private static int XaxisLabels(float time){
        int labels = 7;

        if(0 <= time && time < TimeConversions.ONE_DAY_MILLI )
            labels = 1;
        else if(TimeConversions.ONE_DAY_MILLI <= time  && time <= TimeConversions.SEVEN_DAYS_MILLI - TimeConversions.ONE_DAY_MILLI)
            labels = (int)(time/TimeConversions.ONE_DAY_MILLI) + 1;
        else if(TimeConversions.SIX_MONTHS_MILLI < time && time <= TimeConversions.ONE_YEAR_MILLI) {
            labels = (int) (time / TimeConversions.ONE_MONTH_MILLI);
        if (labels > 8)
            labels = labels/2;
        }

        return labels;
    }
}
