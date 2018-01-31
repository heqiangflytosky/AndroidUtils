package com.android.hq.androidutils.utils;

import android.content.Context;

/**
 * Created by heqiang on 18-1-3.
 * 获取一个Application Context
 */

public class AppContextUtils {
    private static Context mAppContext;

    public static void setAppContext(Context appContext) {
        mAppContext = appContext;
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
