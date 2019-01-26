package com.drcir.weighttracker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChartUtils {

    public static XAxis setXaxisScale(XAxis xAxis, float scale, long dataStartDate){
        if(scale < TimeConversions.THREE_MONTHS_FLOAT + .1) {
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterShort(dataStartDate));
        }
        else if(scale < TimeConversions.ONE_YEAR_FLOAT+ .1) {
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterMedium(dataStartDate));
        }
        else{
            xAxis.setValueFormatter(new AxisValueFormatter.XaxisValueFormatterLong(dataStartDate));
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

    public static LineChart updateChartViewportFullscreen(LineChart chart, float scale){
        //get current right side of viewport
        float highViewX = chart.getHighestVisibleX();
        //resets entire chart
        chart.fitScreen();
        //Sets viewport size for selected scale
        chart.setVisibleXRangeMaximum(scale);
        if(highViewX - scale < 0f)
            chart.moveViewToX(0f);
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

        if(0 <= time && time < TimeConversions.ONE_DAY_FLOAT )
            labels = 3;
        else if(TimeConversions.ONE_DAY_FLOAT <= time  && time <= TimeConversions.SEVEN_DAYS_FLOAT + .09 - TimeConversions.ONE_DAY_FLOAT)
            labels = (int)(time/TimeConversions.ONE_DAY_FLOAT) + 1;
        else if(TimeConversions.SIX_MONTHS_FLOAT +.09 < time && time <= TimeConversions.ONE_YEAR_FLOAT +.09) {
            labels = (int) (time / TimeConversions.ONE_MONTH_FLOAT);
        if (labels > 8)
            labels = labels/2;
        }
        return labels;
    }
}
