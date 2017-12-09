package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by heqiang on 17-6-20.
 *
 * 设备是否支持闪光灯
 *
 */

public class DeviceUtil {
    public static boolean hasCarameFlash(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
