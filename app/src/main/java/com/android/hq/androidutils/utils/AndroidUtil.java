package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.net.URISyntaxException;

/**
 * Created by heqiang on 17-9-1.
 *
 * 获取应用是否创建桌面快捷方式
 * 获取应用的权限信息：包括权限列表，及其所在的权限分组等
 *
 */

public class AndroidUtil {
    private static final String LAUNCH_ACTION = "android.intent.action.MAI";
    public static boolean hasShortcut(Context context, String packageName){
        String uriStr;

        // Android 原生 Launcher包名，一些Rom可能会修改
        if (Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?Notify=true";
        } else if (Build.VERSION.SDK_INT < 19) {
            uriStr = "content://com.android.launcher2.settings/favorites?Notify=true";
        } else {
            uriStr = "content://com.android.launcher3.settings/favorites?Notify=true";
        }

        Uri uri = Uri.parse(uriStr.toString());
        String[] projection = new String[]{"intent"};
        String selection = "itemType=1";
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);
        if (cursor == null) {
            return false;
        }
        try {
            while (cursor.moveToNext()) {
                String intentText = cursor.getString(0);
                try {
                    Intent intent = Intent.parseUri(intentText, 0);
                    if (LAUNCH_ACTION.equals(intent.getAction())) {
                        String appName = intent.getStringExtra("component");
                        if (TextUtils.equals(appName, packageName)) {
                            return true;
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    public static void  getPermissionInfo(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }

        if (packageInfo != null) {
            for (String requestedPerm : packageInfo.requestedPermissions) {
                Log.e("Test","permission = "+requestedPerm);

                try {
                    PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(requestedPerm, 0);
                    Log.e("Test","per des = "+permissionInfo.loadDescription(context.getPackageManager()));
                    Log.e("Test","per label = "+permissionInfo.loadLabel(context.getPackageManager()));
                    Log.e("Test","per des content = "+permissionInfo.describeContents());

                    Log.e("Test","group = "+permissionInfo.group);
                    PermissionGroupInfo groupInfo = context.getPackageManager().getPermissionGroupInfo(permissionInfo.group, 0);
                    Log.e("Test","per group dsc = "+groupInfo.nonLocalizedDescription);
                    Log.e("Test","per group dsc2 = "+groupInfo.loadDescription(context.getPackageManager()));
                    Log.e("Test","per group lable = "+groupInfo.loadLabel(context.getPackageManager()));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
