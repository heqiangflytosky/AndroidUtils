package com.android.hq.androidutils.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.hq.androidutils.R;

/**
 * Created by heqiang on 17-5-2.
 *
 * 锁定当前屏幕方向禁止旋转
 * 解除屏幕方向锁定
 * 显示或者隐藏状态栏
 * 修改Activity样式为弹框样式
 */

public class ActivityUtil {

    private static Activity mActivity;
    public static final double SMALL_WIN_H_SCALE = 0.66796875;
    public static final double SMALL_WIN_W_SCALE = 0.71875;

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

    public static void showStatusBar(boolean show){
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

    /**
     * 修改Activity为弹框样式
     * @param activity
     */
    public static void changeActivitySize(Activity activity) {
        //设置Activity的window参数
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        activity.setFinishOnTouchOutside(true);
        //设置暗度
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        layoutParams.copyFrom(activity.getWindow().getAttributes());
        layoutParams.gravity = Gravity.CENTER;
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        //layoutParams.x = 216;//px
        layoutParams.width = (int) (displayMetrics.widthPixels * SMALL_WIN_W_SCALE);//px
        layoutParams.height = (int) (displayMetrics.heightPixels * SMALL_WIN_H_SCALE);//px
        //设置暗度
        layoutParams.dimAmount = 0.7f;
        layoutParams.alpha = 1.0f;
        activity.getWindow().setAttributes(layoutParams);
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = activity.getWindow().getDecorView();
        if(view != null) {
            view.setBackgroundResource(R.drawable.dialog_activity_bg);
        }
    }

    /**
     * 修改Activity为弹框样式,强制竖屏模式显示.
     * @param activity
     */
    public static void changeActivitySizeFocusPortrait(Activity activity) {
        //设置Activity的window参数
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        activity.setFinishOnTouchOutside(true);
        //设置暗度
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        layoutParams.copyFrom(activity.getWindow().getAttributes());
        layoutParams.gravity = Gravity.CENTER;
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        //layoutParams.x = 216;//px
        if(displayMetrics.widthPixels > displayMetrics.heightPixels){
            layoutParams.width = (int) (displayMetrics.heightPixels * SMALL_WIN_W_SCALE);//px
            layoutParams.height = (int) (displayMetrics.widthPixels * SMALL_WIN_H_SCALE);//px
        }else{
            layoutParams.width = (int) (displayMetrics.widthPixels * SMALL_WIN_W_SCALE);//px
            layoutParams.height = (int) (displayMetrics.heightPixels * SMALL_WIN_H_SCALE);//px
        }
        //设置暗度
        layoutParams.dimAmount = 0.7f;
        layoutParams.alpha = 1.0f;
        activity.getWindow().setAttributes(layoutParams);
        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = activity.getWindow().getDecorView();
        if(view != null) {
            view.setBackgroundResource(R.drawable.dialog_activity_bg);
        }
    }
}
