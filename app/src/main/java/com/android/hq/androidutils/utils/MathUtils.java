package com.android.hq.androidutils.utils;

import android.util.Log;

import java.text.DecimalFormat;

public class MathUtils {
    public static void formatFloat() {
        // float型 保留两位小数
        DecimalFormat df = new DecimalFormat( "#0.00" );
        float f = 5.5555f;
        Log.e("Test",df.format( f ) ); // 5.56
        // float型，小数部分为0，则不显示小数部分
        DecimalFormat df1 = new DecimalFormat( "#0.#####" );
        float f1 = 5.55f;
        float f2 = 5.0f;
        Log.e("Test",df1.format( f1 ) ); // 5.55
        Log.e("Test",df1.format( f2 ) ); // 5
    }
}
