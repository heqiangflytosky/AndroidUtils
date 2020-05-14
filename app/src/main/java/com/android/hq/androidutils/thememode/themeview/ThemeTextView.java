package com.android.hq.androidutils.thememode.themeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.android.hq.androidutils.R;
import com.android.hq.androidutils.thememode.themeutil.ThemeHelper;
import com.android.hq.androidutils.thememode.themeutil.ThemeMode;
import com.android.hq.androidutils.thememode.themeutil.ThemeObserver;

public class ThemeTextView extends AppCompatTextView implements IThemeView {

    private ThemeObserver mThemeObserver;
    private int mDayTextColor;
    private int mNightTextColor;
    private Drawable mDayBackGround;
    private Drawable mNightBackGround;
    private float mDayAlpha;
    private float mNightAlpha = -1;
    private static final int DEFAULT_COLOR = 0;

    public ThemeTextView(Context context) {
        this(context, null);
    }

    public ThemeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDayTextColor = getCurrentTextColor();
        mDayBackGround = getBackground();
        mDayAlpha = getAlpha();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NightTheme);
        mNightTextColor = ta.getColor(R.styleable.NightTheme_nightTextColor, DEFAULT_COLOR);
        mNightBackGround = ta.getDrawable(R.styleable.NightTheme_nightBackground);
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
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (ThemeMode.DAY_MODE.equals(ThemeHelper.getInstance().getThemeMode())) {
            mDayTextColor = getCurrentTextColor();
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (ThemeMode.DAY_MODE.equals(ThemeHelper.getInstance().getThemeMode())) {
            mDayTextColor = getCurrentTextColor();
        }
    }

    @Override
    public void updateTheme(ThemeMode themeMode) {
        if (ThemeMode.DAY_MODE.equals(themeMode)) {
            if (mNightTextColor != DEFAULT_COLOR) {
                setTextColor(mDayTextColor);
            }
            if (mNightBackGround != null) {
                setBackground(mDayBackGround);
            }
            if (mNightAlpha >= 0) {
                setAlpha(mDayAlpha);
            }
        } else if (ThemeMode.NIGHT_MODE.equals(themeMode)) {
            if (mNightTextColor != DEFAULT_COLOR) {
                setTextColor(mNightTextColor);
            }
            if (mNightBackGround != null) {
                setBackground(mNightBackGround);
            }
            if (mNightAlpha >= 0) {
                setAlpha(mNightAlpha);
            }
        }
    }
}
