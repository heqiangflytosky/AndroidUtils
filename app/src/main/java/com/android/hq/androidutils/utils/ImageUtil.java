package com.android.hq.androidutils.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by heqiang on 18-12-21.
 *
 * 合成两张图片
 *
 */

public class ImageUtil {
    /**
     * 将mask覆盖在bitmap之上，并合成指定大小的图片
     * @param bitmap
     * @param mask
     * @param destW
     * @param desH
     * @return
     */
    public static Bitmap maskBitmap(Bitmap bitmap, Bitmap mask, int destW, int desH) {
        if (null == bitmap || mask == null) {
            return null;
        }

        int widthMask = mask.getWidth();
        int heightMask = mask.getHeight();

        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        int width = Math.max(widthMask, widthOrg);
        int height = Math.max(heightMask, heightOrg);

        // 定义期望大小的bitmap
        Bitmap dstBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstBmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 定义需要绘制的某图片上的那一部分矩形空间
        Rect srcMask = new Rect(0, 0, widthMask, heightMask);
        // 定义需要将上面的矩形绘制成新的矩形大小
        Rect dstMask = new Rect((width - widthMask) / 2,
                (height - heightMask) / 2, (width + widthMask) / 2,
                (height + heightMask) / 2);
        canvas.drawBitmap(mask, srcMask, dstMask, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

        // 定义需要绘制的某图片上的那一部分矩形空间
        Rect srcOrg = new Rect(0, 0, widthOrg, heightOrg);
        // mx3绘制成100*100 mx2绘制成80*80
        // 定义需要将上面的矩形绘制成新的矩形大小
        Rect dstOrg = new Rect((width - destW) / 2,
                (height - desH) / 2, (width + destW) / 2,
                (height + desH) / 2);
        // 在已经绘制的mask上叠加bitmap
        canvas.drawBitmap(bitmap, srcOrg, dstOrg, paint);
        return dstBmp;
    }
}
