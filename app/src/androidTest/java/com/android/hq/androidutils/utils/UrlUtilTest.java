package com.android.hq.androidutils.utils;

import android.support.test.annotation.UiThreadTest;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlUtilTest {

    @Test
    public void test() {
        String name = AppUtil.getCurrentProcessName();
        Log.e("Test",name);
    }

}