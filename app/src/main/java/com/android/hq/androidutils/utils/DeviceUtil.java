package com.android.hq.androidutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.util.List;

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
    public static int getScreenHeight(Context context) {
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

    /**
     *
     * @param activity
     * @return View 布局区域高度
     */
    public static int getContentHeight(Activity activity) {
        Rect rectangle = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rectangle);
        return rectangle.height();
    }

    @RequiresApi(Build.VERSION_CODES.P)
    public static boolean isCutoutScreen(@NonNull Context context, @NonNull Window window) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Android P 以下参考各家手机厂商自己实现
            return false;
        } else {
            View decorView = window.getDecorView();
            if (decorView == null) {
                return false;
            }

            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();

            if (rootWindowInsets == null) {
                return false;
            }

            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();

            if (displayCutout == null) {
                return false;
            }

            List<Rect> boundingRects = displayCutout.getBoundingRects();
            if (boundingRects == null || boundingRects.size() <= 0) {
                return false;
            }
            return true;

            /* 这种方式也ok
            boolean hascutout = false;
            try {
                final Resources res = context.getResources();
                final int resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android");
                final String spec = resId > 0 ? res.getString(resId) : null;
                hascutout = spec != null && !TextUtils.isEmpty(spec);
            } catch (Exception e) {
                Log.w("fish", "Can not update hasDisplayCutout. " +
                        e.toString());
            }
            return hascutout;
             */
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    public static int getCutoutHeight(@NonNull Context context, @NonNull Window window) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Android P 以下参考各家手机厂商自己实现
            return 0;
        } else {
            View decorView = window.getDecorView();
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return 0;
            }
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            if (displayCutout == null) {
                return 0;
            }
            return displayCutout.getSafeInsetTop();
        }
    }

    // 全屏模式下填充异形屏的区域
    @RequiresApi(Build.VERSION_CODES.P)
    public static void adapterCutoutScreen(@NonNull Context context, @NonNull Window window) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Android P 以下参考各家手机厂商自己实现
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
    }
}
