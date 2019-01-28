package com.drcir.weighttracker;

public class TimeConversions {
    public TimeConversions() {}

    final static long ONE_MINUTE_MILLI = 60000;
    final static long ONE_HOUR_MILLI = ONE_MINUTE_MILLI * 60;
    final static long ONE_DAY_MILLI = ONE_MINUTE_MILLI * 60 * 24;
    final static long SEVEN_DAYS_MILLI = ONE_DAY_MILLI * 7;
    final static long ONE_MONTH_MILLI = ONE_DAY_MILLI * 30;
    final static long THREE_MONTHS_MILLI = ONE_DAY_MILLI * 91;
    final static long SIX_MONTHS_MILLI = ONE_DAY_MILLI * 182;
    final static long ONE_YEAR_MILLI = ONE_DAY_MILLI * 365;

    final static float ONE_DAY_FLOAT = .1f;
    final static float SEVEN_DAYS_FLOAT = .7f;
    final static float ONE_MONTH_FLOAT = 3.0f;
    final static float THREE_MONTHS_FLOAT = 9.1f;
    final static float SIX_MONTHS_FLOAT = 18.2f;
    final static float ONE_YEAR_FLOAT = 36.5f;

    final static float TOKEN_REFRESH_TIME = ONE_HOUR_MILLI * 6;
}
