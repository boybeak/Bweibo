package com.beak.bweibo.utils;

import android.content.Context;

import com.beak.bweibo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gaoyunfei on 15/6/4.
 */
public class DateHelper {

    public static final SimpleDateFormat
            FORMAT_A = new SimpleDateFormat("HH:mm"),
            FORMAT_B = new SimpleDateFormat("MM dd HH:mm"),
            FORMAT_C = new SimpleDateFormat("yyyy MM dd HH:mm");

    public static final long
            MINUTE_LONG = 60 * 1000,
            HOUR_LONG = 60 * MINUTE_LONG,
            DAY_LONG = 24 * HOUR_LONG;

    public static String formatDateForStatus(Context context, Date date) {
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long delta = now - time;
        if (delta < 0) {
            return context.getString(R.string.time_future);
        } else if (delta >= 0 && delta < MINUTE_LONG) {
            return context.getString(R.string.time_just_now);
        } else if (delta >= MINUTE_LONG && delta < HOUR_LONG) {
            int minutes = (int)(delta / MINUTE_LONG);
            return context.getString(R.string.time_minutes_ago, minutes);
        } else if (delta >= HOUR_LONG && delta < DAY_LONG) {
            return FORMAT_A.format(date);
        } else if (delta >= DAY_LONG) {
            return FORMAT_B.format(date);
        }
        return FORMAT_C.format(date);
    }

}
