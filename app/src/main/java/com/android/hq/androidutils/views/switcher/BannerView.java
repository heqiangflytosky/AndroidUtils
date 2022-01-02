package com.android.hq.androidutils.views.switcher;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.android.hq.androidutils.R;
import com.android.hq.androidutils.views.banner.BannerAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;

public class BannerView extends FrameLayout implements Handler.Callback {
    private static final int MSG_AUTO_PLAY = 1;
    private static final int INTERVAL = 2000;

    private LinearLayout mIndicatorLayout;
    private ViewSwitcher mViewSwitcher;
    private Handler mHandler;
    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private void startAutoPlay() {
        if (mHandler.hasMessages(MSG_AUTO_PLAY)) {
            mHandler.removeMessages(MSG_AUTO_PLAY);
        }
        mHandler.sendEmptyMessageDelayed(MSG_AUTO_PLAY, INTERVAL);
    }

    private void stopAutoPlay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void init(Context context) {
        Fresco.initialize(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_banner, this, true);
        mViewSwitcher = view.findViewById(R.id.view_switcher);
        mIndicatorLayout = view.findViewById(R.id.indicator);

        mHandler = new Handler(Looper.getMainLooper(), this);
    }
}
