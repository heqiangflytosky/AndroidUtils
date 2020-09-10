package com.android.hq.androidutils.views.banner;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.hq.androidutils.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.reflect.Field;

/**
 * 轮播组件
 * 参考：https://github.com/youth5201314/banner
 * https://github.com/pinguo-zhouwei/MZBannerView
 * https://www.sohu.com/a/364998362_671494   https://github.com/zhpanvip/BannerViewPager/
 * https://blog.csdn.net/utomi/article/details/104287989   https://github.com/zguop/banner
 *
 * @author heqiang
 */

public class BannerView extends FrameLayout implements Handler.Callback{
    private boolean mAttrIsShowIndicator;
    private boolean mAttrIsLoop;
    private boolean mAttrAutoPlay;
    private int mAttrIndicatorColor;
    private int mAttrSelectIndicatorColor;
    private int mAttrIndicatorSize;
    private int mAttrIndicatorLayoutGravity;

    private static final int MSG_AUTO_PLAY = 1;
    private long mInterval = 2 * 1000;

    private ViewPager2 mViewPager;
    private BannerAdapter mAdapter;
    private LinearLayout mIndicatorLayout;
    private Handler mHandler;

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAttrAutoPlay) {
            startAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttrAutoPlay) {
            stopAutoPlay();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAttrAutoPlay) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mAttrAutoPlay && msg.what == MSG_AUTO_PLAY) {
            int next = mViewPager.getCurrentItem()+1;
            boolean smooth = true;
            if (mViewPager.getAdapter().getItemCount() == next) {
                next = 0;
                smooth = false;
            }
            mViewPager.setCurrentItem(next,smooth);
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_PLAY, mInterval);
            return true;
        }
        return false;
    }

    public void setData(SparseArray list) {
        mAdapter = new BannerAdapter(mAttrIsLoop, list);
        mViewPager.setAdapter(mAdapter);
        setIndicator();
    }

    private void init(Context context) {
        Fresco.initialize(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_banner, this, true);
        mViewPager = view.findViewById(R.id.view_pager);
        mIndicatorLayout = view.findViewById(R.id.indicator);

        initViewPager();

        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    private void initViewPager() {
        //去掉底部回弹效果
        mViewPager.getChildAt(0).setOverScrollMode(OVER_SCROLL_NEVER);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSelectedIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        initViewPagerScrollProxy();
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

    private void startAutoPlay() {
        if (mHandler.hasMessages(MSG_AUTO_PLAY)) {
            mHandler.removeMessages(MSG_AUTO_PLAY);
        }
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_PLAY, mInterval);
    }

    private void stopAutoPlay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            RecyclerView recyclerView = (RecyclerView) mViewPager.getChildAt(0);
            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(getContext(), mViewPager.getOrientation());
            recyclerView.setLayoutManager(proxyLayoutManger);

            Field LayoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
            LayoutMangerField.setAccessible(true);
            LayoutMangerField.set(mViewPager, proxyLayoutManger);

            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
            pageTransformerAdapterField.setAccessible(true);
            Object mPageTransformerAdapter = pageTransformerAdapterField.get(mViewPager);
            if (mPageTransformerAdapter != null) {
                Class<?> aClass = mPageTransformerAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger);
            }
            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
            scrollEventAdapterField.setAccessible(true);
            Object mScrollEventAdapter = scrollEventAdapterField.get(mViewPager);
            if (mScrollEventAdapter != null) {
                Class<?> aClass = mScrollEventAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class ProxyLayoutManger extends LinearLayoutManager {

        ProxyLayoutManger(Context context, int orientation) {
            super(context, orientation, false);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForDeceleration(int dx) {
                    return (int) (800 * (1 - 0.3356));
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }
}
