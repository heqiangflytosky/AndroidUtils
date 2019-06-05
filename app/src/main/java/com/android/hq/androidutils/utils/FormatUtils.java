package com.android.hq.androidutils.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.BidiFormatter;
import android.text.TextUtils;
import android.view.View;

import com.android.hq.androidutils.R;

import java.util.Locale;

/**
 * 参考 Formatter 类
 * 字节数据格式化，转换为 KB，M，G等
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
}
