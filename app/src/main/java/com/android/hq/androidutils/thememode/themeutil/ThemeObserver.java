package com.android.hq.androidutils.thememode.themeutil;

import android.support.annotation.NonNull;

import com.android.hq.androidutils.thememode.themeview.IThemeView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ThemeObserver {

    private WeakReference<IThemeView> mTarget;
    private Disposable mDisposable;

    public ThemeObserver(@NonNull IThemeView view) {
        mTarget = new WeakReference<>(view);
    }

    public static ThemeObserver onViewCreate(@NonNull IThemeView view) {
        if (ThemeHelper.getInstance().isNightModeEnabled()) {
            return new ThemeObserver(view);
        }

        return null;
    }

    public void onViewAttached(@NonNull IThemeView view) {
        view.updateTheme(ThemeHelper.getInstance().getThemeMode());

        mDisposable = ThemePublisher.getInstance().toDisposable(ThemeMode.class, new Consumer<ThemeMode>() {
            @Override
            public void accept(ThemeMode themeMode) throws Exception {
                IThemeView view = mTarget.get();
                if (view != null) {
                    view.updateTheme(themeMode);
                }
            }
        });
    }

    public void onViewDetached() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
