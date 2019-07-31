package com.android.hq.androidutils.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {
    private static volatile Handler sMainHandler;
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnUiThread(Runnable runnable) {
        if (isInMainThread()) {
            runnable.run();
            return;
        }

        if (sMainHandler == null) {
            synchronized (ThreadUtils.class) {
                if (sMainHandler == null) {
                    sMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        sMainHandler.post(runnable);
    }
}
