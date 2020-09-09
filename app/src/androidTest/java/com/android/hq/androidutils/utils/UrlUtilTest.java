package com.android.hq.androidutils.utils;

import androidx.test.annotation.UiThreadTest;
import android.util.Log;

import org.junit.Test;

public class UrlUtilTest {

    @Test
    public void test() {
        String name = AppUtil.getCurrentProcessName();
        Log.e("Test",name);
    }

}