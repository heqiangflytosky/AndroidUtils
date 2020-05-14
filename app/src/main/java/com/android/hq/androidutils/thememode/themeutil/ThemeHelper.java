package com.android.hq.androidutils.thememode.themeutil;


public class ThemeHelper {
    private static ThemeHelper sThemeHelper;

    private boolean mEnableNightMode = false;
    private ThemeMode mMode = ThemeMode.DAY_MODE;

    private static class ThemeHelperHolder{
        private static ThemeHelper HOLDER = new ThemeHelper();
    }

    public static ThemeHelper getInstance() {
        return ThemeHelperHolder.HOLDER;
    }

    public void  enableNight(){
        mEnableNightMode = true;
    }

    public boolean isNightModeEnabled() {
        return mEnableNightMode;
    }

    public void setThemeMode(ThemeMode mode) {
        mMode = mode;
        ThemePublisher.getInstance().post(mode);
    }

    public ThemeMode getThemeMode() {
        return mMode;
    }
}
