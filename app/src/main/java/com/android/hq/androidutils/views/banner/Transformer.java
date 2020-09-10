package com.android.hq.androidutils.views.banner;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class Transformer {
    public static class ScaleTransformer implements ViewPager2.PageTransformer {
        private static final float DEFAULT_CENTER = 0.5f;
        private float mMinScale = 0.85f;
        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            page.setPivotY(pageHeight >> 1);
            page.setPivotX(pageWidth >> 1);
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setScaleX(mMinScale);
                page.setScaleY(mMinScale);
                page.setPivotX(pageWidth);
            } else if (position <= 1) {
                // [-1,1]
                // Modify the default slide transition to shrink the page as well
                if (position < 0) {
                    //1-2:1[0,-1] ;2-1:1[-1,0]
                    float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));
                } else {
                    //1-2:2[1,0] ;2-1:2[0,1]
                    float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));
                }
            } else {
                // (1,+Infinity]
                page.setPivotX(0);
                page.setScaleX(mMinScale);
                page.setScaleY(mMinScale);
            }
        }
    }
}
