package com.android.hq.androidutils.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by heqiang on 17-2-6.
 *
 * 获得当前应用程序的名称
 * 获得当前应用程序的版本号
 * 通过包名启动App
 * 获取系统已经安装应用信息
 * 通过Intent启动Activity
 * 通过url启动Activity
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
    public static void loadAllApps(Context context){
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

    /**
     *  通过Intent启动Activity
     */
    public static boolean startAppByIntent(Activity activity, Intent intent) {
        try {
            activity.startActivityIfNeeded(intent, -1);
            return true;
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 通过url启动Activity
     */

    public static boolean startActivityByUrl(Activity activity){
        //启动电话
        //String url = "tel:123456789";
        //如果安装了唯品会应用，启动唯品会
        String url = "vipshop://goHome?f=fx&tra_from=tra%3Awvpeo8om%3Anm5fj2y6%3Aq15uwfdz%3Agtpgmchu%3A%3A4gl0mmyi%3A%3A3739c1c4dd4d4e78889982e23609e056";
        Intent intent = null;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(activity.getPackageManager().resolveActivity(intent, 0) != null){
            return startAppByIntent(activity, intent);
        }else{
            String packagename = intent.getPackage();
            if (packagename != null) {
                intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://search?q=pname:" + packagename));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
