package com.android.hq.androidutils.views.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BannerViewPager extends ViewPager implements Handler.Callback {
    private static final int MSG_AUTO_PLAY = 1;
    private long mInterval = 2 * 1000;
    private boolean mAutoPlay;
    private boolean mLoop;

    private Handler mHandler;
    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mAutoPlay && msg.what == MSG_AUTO_PLAY) {
            int next = getCurrentItem()+1;
            boolean smooth = true;
            if (getAdapter().getCount() == next) {
                next = 0;
                smooth = false;
            }
            setCurrentItem(next,smooth);
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_PLAY, mInterval);
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAutoPlay) {
            startAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAutoPlay) {
            stopAutoPlay();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlay) {
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

    public void setAutoPlay(boolean autoScroll) {
        mAutoPlay = autoScroll;
    }

    public boolean isAutoPlay() {
        return mAutoPlay;
    }

    public void startAutoPlay() {
        if (mHandler.hasMessages(MSG_AUTO_PLAY)) {
            mHandler.removeMessages(MSG_AUTO_PLAY);
        }
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_PLAY, mInterval);
    }

    public void stopAutoPlay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
}
