package com.mvp.fonts.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int getLengthByDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        int multiple;
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            multiple = (int) (size.x * 0.3);
        } else {
            multiple = (size.x);
        }
        return multiple;
    }

    public static String printStackTrace(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    public static String getStackTraceElementString(StackTraceElement[] stack) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement s : stack)
            sb.append(s.toString() + "\n\t\t");

        return sb.toString();
    }

    public static boolean isAbsEmpty(String text) {
        return TextUtils.isEmpty(text) || text.toUpperCase().contentEquals("NULL") || text.length() == 0;
    }

    public static int dp2Px(Context ctx, int value) {
        int ret = 0;
        if (ctx != null) {
            ret = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, ctx.getResources().getDisplayMetrics());
        }
        return ret;
    }
}
