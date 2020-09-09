package com.android.hq.androidutils.views.banner;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.hq.androidutils.R;

/**
 * 轮播组件
 * 参考：https://github.com/youth5201314/banner
 * https://github.com/pinguo-zhouwei/MZBannerView
 * https://www.sohu.com/a/364998362_671494   https://github.com/zhpanvip/BannerViewPager/
 *https://blog.csdn.net/utomi/article/details/104287989   https://github.com/zguop/banner
 * @author heqiang
 */

public class BannerView extends FrameLayout {
    private boolean mAttrIsShowIndicator;
    private boolean mAttrIsLoop;
    private boolean mAttrAutoPlay;
    private int mAttrIndicatorColor;
    private int mAttrSelectIndicatorColor;
    private int mAttrIndicatorSize;
    private int mAttrIndicatorLayoutGravity;

    private BannerViewPager mViewPager;
    private BannerAdapter mAdapter;
    private LinearLayout mIndicatorLayout;

    public enum IndicatorLayoutGravity{
        LEFT,
        CENTER,
        RIGHT
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_banner, this, true);
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicatorLayout = view.findViewById(R.id.indicator);

        mAdapter = new BannerAdapter(mViewPager, mAttrIsLoop);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setLoop(mAttrIsLoop);
        mViewPager.setAutoPlay(mAttrAutoPlay);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setIndicator();
    }

    private void readAttrs(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);

        mAttrIsShowIndicator = typedArray.getBoolean(R.styleable.BannerView_indicator, true);
        mAttrIsLoop = typedArray.getBoolean(R.styleable.BannerView_loop, true);
        mAttrAutoPlay = typedArray.getBoolean(R.styleable.BannerView_auto_play, true);
        mAttrIndicatorColor = typedArray.getColor(R.styleable.BannerView_indicator_color, 0x50000000);
        mAttrSelectIndicatorColor = typedArray.getColor(R.styleable.BannerView_indicator_selected_color, 0xff33b4ff);
        mAttrIndicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerView_indicator_size,10);
        mAttrIndicatorLayoutGravity = typedArray.getInt(R.styleable.BannerView_indicator_layout_gravity, IndicatorLayoutGravity.CENTER.ordinal());

        typedArray.recycle();
    }

    private void setIndicator() {

        if (mAttrIsShowIndicator) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mIndicatorLayout.getLayoutParams();
            if (mAttrIndicatorLayoutGravity == IndicatorLayoutGravity.LEFT.ordinal()) {
                layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
            } else if (mAttrIndicatorLayoutGravity == IndicatorLayoutGravity.CENTER.ordinal()) {
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            } else if (mAttrIndicatorLayoutGravity == IndicatorLayoutGravity.RIGHT.ordinal()) {
                layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            }

            mIndicatorLayout.setVisibility(VISIBLE);
            while(mIndicatorLayout.getChildCount() < mAdapter.getRealCount()) {
                addIndicatorPoint();
            }
        } else {
            mIndicatorLayout.removeAllViews();
            mIndicatorLayout.setVisibility(GONE);
        }
    }

    private void addIndicatorPoint() {
        IndicatorPoint indicatorPoint = new IndicatorPoint(getContext());
        indicatorPoint.setSize(mAttrIndicatorSize);
        indicatorPoint.setColor(mAttrIndicatorColor);
        indicatorPoint.setSelectedColor(mAttrSelectIndicatorColor);
        mIndicatorLayout.addView(indicatorPoint, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        int index = mIndicatorLayout.indexOfChild(indicatorPoint);
        if (index == mViewPager.getCurrentItem()) {
            indicatorPoint.setSelected(true);
        } else {
            indicatorPoint.setSelected(false);
        }
    }

    private void setSelectedIndicator(int current) {
        for (int i = 0; i < mIndicatorLayout.getChildCount(); i++) {
            IndicatorPoint point = (IndicatorPoint) mIndicatorLayout.getChildAt(i);
            if (i == current) {
                point.setSelected(true);
            } else {
                point.setSelected(false);
            }
        }
    }
}
