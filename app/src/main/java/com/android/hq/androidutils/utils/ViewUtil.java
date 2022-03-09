package com.android.hq.androidutils.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 按比例缩放View
 * 按比例缩放，保持宽高的最大值和原来相同
 */

public class ViewUtil {
    /**
     * 按比例缩放View
     * @param context
     * @param player
     * @param videoWidth
     * @param videoHeight
     * @param playerWidth
     * @param playerHeight
     */
    public static void updateVideoSize(Context context, View player, int videoWidth, int videoHeight, int playerWidth, int playerHeight){
        if(context == null || player == null) {
            return;
        }
        int viewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int viewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        if(videoWidth>0 && videoHeight>0){
            float videoAspect = ((float)videoWidth)/videoHeight;
            float playerAspect = ((float)playerWidth)/playerHeight;
            if(videoAspect > playerAspect){
                viewWidth = playerWidth;
                viewHeight = (int)(playerWidth * (1/videoAspect));
            }else{
                viewWidth = (int)(playerHeight * videoAspect);
                viewHeight = playerHeight;
            }
        }
        ViewGroup.LayoutParams lp = player.getLayoutParams();
        lp.height=viewHeight;
        lp.width=viewWidth;
        player.setLayoutParams(lp);
        player.requestLayout();
    }

    /**
     * 按比例缩放，保持宽高的最大值和原来相同
     * @param view
     * @param width
     * @param height
     */
    public void updateViewSize(View view, final int width, final int height) {
        int w = view.getWidth();
        int h = view.getHeight();
        if(width>0 && height>0){
            float textureAspect = ((float)width)/height;
            float viewAspect = ((float)w)/h;
            if(textureAspect > viewAspect){
                h = (int)(w * (1/textureAspect));
            }else{
                w = (int)(h * textureAspect);
            }
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = h;
        view.setLayoutParams(layoutParams);
    }

    public static boolean isTouchInView(View view, MotionEvent ev) {
        int[] temp = new int[2];
        view.getLocationOnScreen(temp);
        final int x = temp[0];
        final int y = temp[1];
        Rect rect = new Rect(x, y, x + view.getWidth(),y + view.getHeight());
        return rect.contains((int)ev.getRawX(),(int)ev.getRawY());
    }
}
