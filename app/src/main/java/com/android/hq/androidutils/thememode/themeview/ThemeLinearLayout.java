package com.android.hq.androidutils.thememode.themeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.android.hq.androidutils.R;
import com.android.hq.androidutils.thememode.themeutil.ThemeMode;
import com.android.hq.androidutils.thememode.themeutil.ThemeObserver;

public class ThemeLinearLayout extends LinearLayout implements IThemeView {
    private Drawable mDayBackground;
    private Drawable mNightBackground;
    private float mDayAlpha;
    private float mNightAlpha = -1;
    private ThemeObserver mThemeObserver;

    public ThemeLinearLayout(Context context) {
        this(context, null);
    }

    public ThemeLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemeLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ThemeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDayBackground = getBackground();
        mDayAlpha = getAlpha();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NightTheme);
        mNightBackground = ta.getDrawable(R.styleable.NightTheme_nightBackground);
        mNightAlpha = ta.getFloat(R.styleable.NightTheme_nightAlpha, -1);

        ta.recycle();
        mThemeObserver = ThemeObserver.onViewCreate(this);
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
            if (mNightBackground != null) {
                setBackground(mDayBackground);
            }

            if (mNightAlpha >= 0) {
                setAlpha(mDayAlpha);
            }
        } else if (ThemeMode.NIGHT_MODE.equals(themeMode)) {
            if (mNightBackground != null) {
                setBackground(mNightBackground);
            }

            if (mNightAlpha >= 0) {
                setAlpha(mNightAlpha);
            }
        }
    }
}
