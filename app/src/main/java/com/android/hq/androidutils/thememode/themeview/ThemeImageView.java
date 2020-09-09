package com.android.hq.androidutils.thememode.themeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.hq.androidutils.R;
import com.android.hq.androidutils.thememode.themeutil.ThemeMode;
import com.android.hq.androidutils.thememode.themeutil.ThemeObserver;

public class ThemeImageView extends ImageView implements IThemeView {
    private ThemeObserver mThemeObserver;
    private float mNightAlpha = -1;
    private static final int DEFAULT_COLOR = 0;
    private int mNightFilterColor;
    private Drawable mDayBackGround;
    private Drawable mNightBackGround;
    private float mDayAlpha;


    public ThemeImageView(Context context) {
        this(context, null);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ThemeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDayBackGround = getBackground();
        mDayAlpha = getAlpha();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NightTheme);
        mNightAlpha = ta.getFloat(R.styleable.NightTheme_nightAlpha, -1);
        mNightFilterColor = ta.getColor(R.styleable.NightTheme_nightFilterColor, DEFAULT_COLOR);
        mNightBackGround = ta.getDrawable(R.styleable.NightTheme_nightBackground);
        ta.recycle();
        mThemeObserver = ThemeObserver.onViewCreate(this);
    }

    public void setNightAlpha(float nightAlpha) {
        mNightAlpha = nightAlpha;
    }

    public void setNightFilterColor(int nightFilterColor) {
        mNightFilterColor = nightFilterColor;
    }

    public void setNightBackGround(Drawable nightBackGround) {
        mNightBackGround = nightBackGround;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mThemeObserver != null) {
            mThemeObserver.onViewAttached(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mThemeObserver != null) {
            mThemeObserver.onViewDetached();
        }
    }

    @Override
    public void updateTheme(ThemeMode themeMode) {
        if (ThemeMode.DAY_MODE.equals(themeMode)) {
            if (mNightAlpha >= 0) {
                setAlpha(mDayAlpha);
            }
            if (mNightFilterColor != DEFAULT_COLOR) {
                clearColorFilter();
            }
            if (mNightBackGround != null) {
                setBackground(mDayBackGround);
            }
        } else {
            if (mNightAlpha >= 0) {
                setAlpha(mNightAlpha);
            }
            if (mNightBackGround != null) {
                setBackground(mNightBackGround);
            }
            if (mNightFilterColor != DEFAULT_COLOR) {
                setColorFilter(mNightFilterColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
