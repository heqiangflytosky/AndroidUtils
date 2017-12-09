package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.net.URISyntaxException;

/**
 * Created by heqiang on 17-9-1.
 *
 * 获取应用是否创建桌面快捷方式
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
}
