package com.beak.beakkit.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by gaoyunfei on 15/7/15.
 */
public class UiUtils {
    public static final String TAG = UiUtils.class.getSimpleName();

    public static int getActionBarHeight (Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        int height = (int)(56 * metrics.density);

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, metrics);
        }

        return height;
    }
}
