package com.beak.beakkit.debug;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gaoyunfei on 15/5/16.
 */
public class Debug {
    public static final String ACTION_DEBUG = "com.beak.beakkit.Debug.action_debug";

    public static final String KEY_DEBUG_MSG = "com.beak.beakkit.Debug.debug_msg";

    private static final String
            COLOR_I = "#55AA66",
            COLOR_E = "#B34D4D",
            COLOR_D = "#4D61B3",
            COLOR_V = "#4D4D4D",
            COLOR_W = "#C7A417";

    private static boolean isDebug = false;

    private static Debug sDebug = null;

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh-mm-ss SSS");

    public static synchronized Debug getInstance (Context context) {
        if (sDebug == null) {
            sDebug = new Debug(context.getApplicationContext());
        }
        return sDebug;
    }

    public static void setDebugMode (boolean debug) {
        isDebug = debug;
    }

    private Context mContext = null;
    private Date mDate = null;

    public Debug (Context context) {
        mContext = context;
        mDate = new Date();
    }

    public void i (String msg) {
        send(msg, COLOR_I);
    }

    public void v (String msg) {
        send(msg, COLOR_V);
    }

    public void d (String msg) {
        send(msg, COLOR_D);
    }

    public void w (String msg) {
        send(msg, COLOR_W);
    }

    public void e (String msg) {
        send(msg, COLOR_E);
    }

    private void send (String msg, String color) {
        if (isDebug) {
            mDate.setTime(System.currentTimeMillis());
            Intent it = new Intent(ACTION_DEBUG);
            it.putExtra(KEY_DEBUG_MSG, Html.fromHtml("<font color=\"" + color + "\">" + TIME_FORMATTER.format(mDate) + "\t" + msg + "<font><br/>"));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
        }
    }
}
