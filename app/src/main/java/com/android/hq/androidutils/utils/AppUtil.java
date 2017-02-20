package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

/**
 * Created by heqiang on 17-2-6.
 *
 * 获得当前应用程序的名称
 * 获得当前应用程序的版本号
 * 通过包名启动App
 */

public class AppUtil {
    private static Context mAppContext;

    private static String sAppName;
    private static String sVerionName;

    public static void init(Context context){
        mAppContext = context;
    }

    /**
     * 获得当前应用程序的名称
     * @return
     */
    public static String getAppName(){
        if(sAppName == null){
            PackageManager packageManager = null;
            ApplicationInfo applicationInfo = null;
            try {
                packageManager = mAppContext.getPackageManager();
                applicationInfo = packageManager.getApplicationInfo(mAppContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                applicationInfo = null;
            }
            sAppName = (String) packageManager.getApplicationLabel(applicationInfo);
        }
        return sAppName;
    }

    /**
     * 获得当前应用程序的版本号
     * @return
     */
    public static String getVersionName(){
        if(sVerionName == null){
            PackageManager packageManager = mAppContext.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(mAppContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(packageInfo != null){
                sVerionName = packageInfo.versionName;
            }
        }
        return sVerionName;
    }

    /**
     * 通过包名启动App
     * @param pkgName
     */
    public static void startAppByPkgName(String pkgName){
        Intent intent =null;

        intent = mAppContext.getPackageManager().getLaunchIntentForPackage(pkgName);

        if(intent != null){
            mAppContext.startActivity(intent);
        }
    }

    /**
     * 获取系统已经安装应用信息
     */
    public void loadAllApps(Context context){
        final PackageManager packageManager = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : apps) {
            Log.e("App","name = " + info.activityInfo.name+", label = "+info.activityInfo.loadLabel(packageManager));
        }

        //另外一种方法
//        final PackageManager packageManager = getPackageManager();
//
//        List<PackageInfo> apps = packageManager.getInstalledPackages(0);
//
//        for (PackageInfo info : apps) {
//            Log.e("App","name = " + info.applicationInfo.packageName+", label = " + info.applicationInfo.loadLabel(packageManager));
//        }
    }
}
