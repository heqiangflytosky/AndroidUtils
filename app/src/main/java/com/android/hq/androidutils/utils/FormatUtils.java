package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.BidiFormatter;
import android.text.TextUtils;
import android.view.View;

import com.android.hq.androidutils.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参考 Formatter 类
 * 字节数据格式化，转换为 KB，M，G等
 * 判断是否为电话号码
 * 日期格式化
 */

public class FormatUtils {
    public static final int FLAG_SHORTER = 1 << 0;
    public static final int FLAG_CALCULATE_ROUNDED = 1 << 1;

    public static final long KB_IN_BYTES = 1024;
    public static final long MB_IN_BYTES = KB_IN_BYTES * 1024;
    public static final long GB_IN_BYTES = MB_IN_BYTES * 1024;
    public static final long TB_IN_BYTES = GB_IN_BYTES * 1024;
    public static final long PB_IN_BYTES = TB_IN_BYTES * 1024;

    public static class BytesResult {
        public final String value;
        public final String units;
        public final long roundedBytes;

        public BytesResult(String value, String units, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.roundedBytes = roundedBytes;
        }
    }

    private static String bidiWrap(@NonNull Context context, String source) {
        final Locale locale = context.getResources().getConfiguration().locale;
        if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL) {
            return BidiFormatter.getInstance(true /* RTL*/).unicodeWrap(source);
        } else {
            return source;
        }
    }

    public static String formatFileSize(@Nullable Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        final BytesResult res = formatBytes(context.getResources(), sizeBytes, 0);
        return bidiWrap(context, context.getString(R.string.fileSizeSuffix,
                res.value, res.units));
    }

    public static String formatShortFileSize(@Nullable Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        final BytesResult res = formatBytes(context.getResources(), sizeBytes, FLAG_SHORTER);
        return bidiWrap(context, context.getString(R.string.fileSizeSuffix,
                res.value, res.units));
    }

    public static BytesResult formatBytes(Resources res, long sizeBytes, int flags) {
        final boolean isNegative = (sizeBytes < 0);
        float result = isNegative ? -sizeBytes : sizeBytes;
        int suffix = R.string.byteShort;
        long mult = 1;
        if (result > 900) {
            suffix = R.string.kilobyteShort;
            mult = KB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.megabyteShort;
            mult = MB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.gigabyteShort;
            mult = GB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.terabyteShort;
            mult = TB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.petabyteShort;
            mult = PB_IN_BYTES;
            result = result / 1024;
        }
        // Note we calculate the rounded long by ourselves, but still let String.format()
        // compute the rounded value. String.format("%f", 0.1) might not return "0.1" due to
        // floating point errors.
        final int roundFactor;
        final String roundFormat;
        if (mult == 1 || result >= 100) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else if (result < 1) {
            roundFactor = 100;
            roundFormat = "%.2f";
        } else if (result < 10) {
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 10;
                roundFormat = "%.1f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        } else { // 10 <= result < 100
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 1;
                roundFormat = "%.0f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        }

        if (isNegative) {
            result = -result;
        }
        final String roundedString = String.format(roundFormat, result);

        // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like 80PB so
        // it's okay (for now)...
        final long roundedBytes =
                (flags & FLAG_CALCULATE_ROUNDED) == 0 ? 0
                        : (((long) Math.round(result * roundFactor)) * mult / roundFactor);

        final String units = res.getString(suffix);

        return new BytesResult(roundedString, units, roundedBytes);
    }

    public static String formatFileSize(long size) {
        float value = size;
        String unit = "B";
        if (value > 1024) {
            value /= 1024;
            unit = "KB";
        }
        if (value > 1024) {
            value /= 1024;
            unit = "MB";
        }
        if (value > 1024) {
            value /= 1024;
            unit = "GB";
        }
        return String.format(Locale.getDefault(), "%.2f %s", value, unit);
    }

    /**
     * 手机号验证
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }


    private static SimpleDateFormat sDataFormatZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat sDataFormat6S = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *
     * @param time 2016-10-09T11:45:38.236Z
     * @return
     */
    public static String formatTime(String time){
        if(TextUtils.isEmpty(time))
            return null;
        //time = time.replace("Z"," UTC");
        Calendar c = null;
        try {
            if(time.endsWith("Z")) {
                sDataFormatZ.parse(time);
                c = sDataFormatZ.getCalendar();
            }
            else {
                sDataFormat.parse(time);
                c = sDataFormat.getCalendar();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if(c != null){
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);

            //获取当前时间
//            Date date = new Date();
//            sDataFormat.format(date);
            c.setTimeInMillis(System.currentTimeMillis());
            int cur_year = c.get(Calendar.YEAR);
            int cur_month = c.get(Calendar.MONTH) + 1;
            int cur_day = c.get(Calendar.DAY_OF_MONTH);
            int cur_hour = c.get(Calendar.HOUR_OF_DAY);
            int cur_minute = c.get(Calendar.MINUTE);
            int cur_second = c.get(Calendar.SECOND);

            if(year < cur_year){
                return (cur_year - year) + "年前";
            } else if(month < cur_month){
                return (cur_month - month) + "个月前";
            } else if(day < cur_day){
                return (cur_day - day) + "天前";
            } else if(hour < cur_hour){
                return (cur_hour - hour) + "小时前";
            } else if(minute < cur_minute){
                return (cur_minute - minute) + "分钟前";
            } else if(second < cur_second){
                return (cur_second - second) + "秒前";
            }
        }
        return null;
    }
}
