package com.android.hq.androidutils.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by heqiang on 18-4-26.
 *
 * 对Url进行参数拼接
 *
 */

public class UrlUtil {
    public static String appendUrl(String baseUrl, Map params) {
        if (params == null || TextUtils.isEmpty(baseUrl)) {
            return null;
        }
        Uri.Builder UriBuilder = Uri.parse(baseUrl).buildUpon();
        Set<Map.Entry<String, String>> entries;
        if (params != null && !params.isEmpty()) {
            entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                UriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return UriBuilder.toString();
    }
}
