package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by heqiang on 17-6-20.
 *
 * 设备是否支持闪光灯
 * 获取设备屏幕宽高
 */

public class DeviceUtil {
    public static boolean hasCarameFlash(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     *
     * @param context
     * @return 屏幕可显示区域的高度，不包括状态栏，导航栏等
     */
    public static int getDisplayHeight(Context context) {
        // 1.
        /*
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
        */

        //2.
        /*
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
        */

        // 3.
        /*
        Rect outSize = new Rect();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRectSize(outSize);
        return outSize.bottom;
        */

        //4.
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     *
     * @param context
     * @return 设备屏幕的高度
     */
    public static int getDisplayHeight2(Context context) {
        //1.
        /*
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealSize(point);
        return point.y;
        */

        //2.
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
