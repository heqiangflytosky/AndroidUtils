package com.android.hq.androidutils.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * 1.避免 Android 7.X 及以下版本的 BadTokenException 问题
 *
 * 测试代码：
 * https://heqiangfly.gitee.io/2018/10/15/android-performance-optimization-toast-badtoken/
 */

public class ToastUtils {
    private static String TAG = "ToastUtils";
    private static Field sField_TN ;
    private static Field sField_TN_Handler ;
    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG,"reflect toast error", e);
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler)sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn,new SafelyHandlerWarpper(preHandler));
        } catch (Exception e) {
            Log.e(TAG,"hook toast error", e);
        }
    }

    public static void showToast(Context context, CharSequence cs, int length) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Toast toast = Toast.makeText(context, cs, length);
            hook(toast);
            toast.show();
        } else {
            Toast.makeText(context,cs,length).show();
        }
    }

    private static class SafelyHandlerWarpper extends Handler {

        private Handler impl;

        public SafelyHandlerWarpper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                Log.e(TAG,"show toast error", e);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
}
