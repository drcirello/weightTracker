package com.drcir.weighttracker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ChartUtils {

    public static XAxis setXaxisScale(XAxis xAxis, float scale){
        if(scale <= TimeConversions.THREE_MONTHS_MILLI) {
            xAxis.setValueFormatter(new MyValueFormatter.MyValueFormatterShort());
            xAxis.setLabelCount(7, true);
        }
        else if(scale < TimeConversions.ONE_YEAR_MILLI) {
            xAxis.setValueFormatter(new MyValueFormatter.MyValueFormatterMedium());
            xAxis.setLabelCount(7, true);
        }
        else{
            xAxis.setValueFormatter(new MyValueFormatter.MyValueFormatterLong());
            xAxis.setLabelCount(7, true);
        }

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
}
