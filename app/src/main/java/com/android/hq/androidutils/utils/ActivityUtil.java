package com.android.hq.androidutils.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by heqiang on 17-5-2.
 *
 * 锁定当前屏幕方向禁止旋转
 * 解除屏幕方向锁定
 * 显示或者隐藏状态栏
 */

public class ActivityUtil {

    private static Activity mActivity;

    public static void init(Activity activity){
        mActivity = activity;
    }

    @SuppressWarnings("all")
    public static void lockScreenOrientation() {
        mActivity.setRequestedOrientation(mapConfigurationOriActivityInfoOri(mActivity.getResources()
                .getConfiguration().orientation));
    }

    public static void unlockScreenOrientation(){
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private static int mapConfigurationOriActivityInfoOri(int configOri) {
        final Display d = mActivity.getWindowManager().getDefaultDisplay();
        int naturalOri = Configuration.ORIENTATION_LANDSCAPE;
        switch (d.getRotation()) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                // We are currently in the same basic orientation as the natural orientation
                naturalOri = configOri;
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                // We are currently in the other basic orientation to the natural orientation
                naturalOri = (configOri == Configuration.ORIENTATION_LANDSCAPE) ?
                        Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
                break;
        }

        int[] oriMap = {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
                ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
                ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        };
        // Since the map starts at portrait, we need to offset if this device's natural orientation
        // is landscape.
        int indexOffset = 0;
        if (naturalOri == Configuration.ORIENTATION_LANDSCAPE) {
            indexOffset = 1;
        }
        return oriMap[(d.getRotation() + indexOffset) % 4];
    }

    private void showStatusBar(boolean show){
        Window window = mActivity.getWindow();
        if (null != window) {
            WindowManager.LayoutParams winParams = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (show) {
                winParams.flags &= ~bits;
            } else {
                winParams.flags |= bits;
            }
            window.setAttributes(winParams);
        }
    }
}
