package com.drcir.weighttracker;

public class TimeConversions {
    public TimeConversions() {};

    final static float ONE_MINUTE_MILLI = 60000;
    final static float ONE_HOUR_MILLI = ONE_MINUTE_MILLI * 60;
    final static float ONE_DAY_MILLI = ONE_MINUTE_MILLI * 60 * 24;
    final static float SEVEN_DAYS_MILLI = ONE_DAY_MILLI * 7;
    final static float ONE_MONTH_MILLI = ONE_DAY_MILLI * 30;
    final static float THREE_MONTHS_MILLI = ONE_DAY_MILLI * 91;
    final static float SIX_MONTHS_MILLI = ONE_DAY_MILLI * 182;
    final static float ONE_YEAR_MILLI = ONE_DAY_MILLI * 365;

    final static float TOKEN_REFRESH_TIME = ONE_HOUR_MILLI * 6;
}
