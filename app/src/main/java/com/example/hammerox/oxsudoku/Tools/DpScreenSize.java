package com.example.hammerox.oxsudoku.Tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;


public class DpScreenSize {

    public DpScreenSize() {
    }

    public float getScreenHeight(Activity activity) {
        Context context = activity.getApplicationContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int heightPx = displaymetrics.heightPixels;
        float heightDp = MetricConverter.convertPixelsToDp(heightPx, context);

        return heightDp;
    }

    public float getScreenWeight(Activity activity) {
        Context context = activity.getApplicationContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthPx = displaymetrics.widthPixels;
        float widthDp = MetricConverter.convertPixelsToDp(widthPx, context);

        return widthDp;
    }

}
