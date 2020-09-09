package com.android.hq.androidutils.thememode;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.hq.androidutils.R;
import com.android.hq.androidutils.thememode.themeutil.ThemeHelper;
import com.android.hq.androidutils.thememode.themeutil.ThemeMode;

public class ThemeModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.getInstance().enableNight();
        setContentView(R.layout.activity_theme_mode);
    }

    public void convertTheme(View view) {
        if (ThemeHelper.getInstance().getThemeMode().equals(ThemeMode.DAY_MODE)) {
            ThemeHelper.getInstance().setThemeMode(ThemeMode.NIGHT_MODE);
        } else {
            ThemeHelper.getInstance().setThemeMode(ThemeMode.DAY_MODE);
        }

    }
}
