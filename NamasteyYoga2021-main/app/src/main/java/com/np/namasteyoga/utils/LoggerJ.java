package com.np.namasteyoga.utils;

import android.util.Log;

import com.np.namasteyoga.BuildConfig;


public class LoggerJ {
    private static final String TAG = "Logger";

    public static void Debug(String tag, String msg) {
        if (tag == null)
            tag = TAG;
        if (C.DEBUG)
            Log.d(tag, "Debug: " + msg);
    }

    public static void Error(String tag, String msg) {
        if (tag == null)
            tag = TAG;
        if (C.DEBUG)
            Log.e(tag, "Debug: " + msg);
    }

    public static void Debug(String tag, Object msg) {
        if (tag == null)
            tag = TAG;
        if (C.DEBUG)
            Log.d(tag, "Debug: " + msg.toString());
    }

    public static void Error(String tag, Object msg) {
        if (tag == null)
            tag = TAG;
        if (C.DEBUG)
            Log.e(tag, "Debug: " + msg.toString());
    }
}
