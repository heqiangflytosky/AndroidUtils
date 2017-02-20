package com.android.hq.androidutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by heqiang on 17-2-6.
 *
 * 检测设备当前网络是否可用
 * 检测当前网络类型(wifi、2g、3g、4g、off、unknown)
 */

public class NetWorkUtil {
    public static final String TYPE_WIFI = "wifi";
    public static final String TYPE_2G = "2g";
    public static final String TYPE_3G = "3g";
    public static final String TYPE_4G = "4g";
    public static final String TYPE_OFF = "off";
    public static final String TYPE_UNKNOWN = "unknown";

    public static boolean isNetworkWorking(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        }
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != wifiInfo && NetworkInfo.State.CONNECTED == wifiInfo.getState()
                || null != mobileInfo && NetworkInfo.State.CONNECTED == mobileInfo.getState()) {
            return true;
        }
        return false;
    }

    public static String getNetworkType(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        return TYPE_WIFI;
                    } else {
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS
                                || tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE
                                || tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA
                                || tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_1xRTT
                                || tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_IDEN) {
                            return TYPE_2G;
                        } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                            return TYPE_4G;
                        } else {
                            return TYPE_3G;
                        }
                    }
                } else {
                    return TYPE_OFF;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TYPE_UNKNOWN;
    }
}
