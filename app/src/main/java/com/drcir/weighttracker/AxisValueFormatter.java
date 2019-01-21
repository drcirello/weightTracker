package com.drcir.weighttracker;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AxisValueFormatter {

    static class XaxisValueFormatterShort implements IAxisValueFormatter {

            private String[] mValues;

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd. MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }

    }

    static class XaxisValueFormatterShortFullscreen implements IAxisValueFormatter {

        private String[] mValues;

        private final SimpleDateFormat mFormat = new SimpleDateFormat("dd. MMM ''yy", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) value;
            return mFormat.format(new Date(millis));
        }

    }

    static class XaxisValueFormatterMedium implements IAxisValueFormatter {

        private String[] mValues;

        private final SimpleDateFormat mFormat = new SimpleDateFormat("MMM ''yy", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) value;
            return mFormat.format(new Date(millis));
        }

    }

    static class XaxisValueFormatterLong implements IAxisValueFormatter {

        private String[] mValues;

        private final SimpleDateFormat mFormat = new SimpleDateFormat("MMM ''yy", Locale.ENGLISH);

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            long millis = (long) value;
            return mFormat.format(new Date(millis));
        }

    }

}
