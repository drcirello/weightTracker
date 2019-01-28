package com.drcir.weighttracker;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AxisValueFormatter {

    static class XaxisValueFormatterShort implements IAxisValueFormatter {


        private String[] mValues;

        private long mStartDate;
        public XaxisValueFormatterShort(long startDate){
            mStartDate = startDate;
        }

        private final SimpleDateFormat mFormat = new SimpleDateFormat("dd. MMM", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) (value * 10 * TimeConversions.ONE_DAY_MILLI) + mStartDate;
            return mFormat.format(new Date(millis));
        }

    }

    static class XaxisValueFormatterMedium implements IAxisValueFormatter {

        private String[] mValues;

        private long mStartDate;
        public XaxisValueFormatterMedium(long startDate){
            mStartDate = startDate;
        }

        private final SimpleDateFormat mFormat = new SimpleDateFormat("MMM ''yy", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) (value * 10 * TimeConversions.ONE_DAY_MILLI) + mStartDate;
            return mFormat.format(new Date(millis));
        }

    }

    static class XaxisValueFormatterLong implements IAxisValueFormatter {

        private String[] mValues;

        private long mStartDate;
        public XaxisValueFormatterLong(long startDate){
            mStartDate = startDate;
        }

        private final SimpleDateFormat mFormat = new SimpleDateFormat("MMM ''yy", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) (value * 10 * TimeConversions.ONE_DAY_MILLI) + mStartDate;
            return mFormat.format(new Date(millis));
        }

    }

}
