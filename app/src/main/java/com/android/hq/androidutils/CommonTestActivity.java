package com.android.hq.androidutils;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.android.hq.androidutils.utils.ActivityUtil;
import com.android.hq.androidutils.utils.AndroidUtil;
import com.android.hq.androidutils.utils.MathUtils;
import com.android.hq.androidutils.views.banner.BannerActivity;

public class CommonTestActivity extends AppCompatActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_test);
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://github.com/heqiangflytosky/AndroidUtils/");

        startTest();
    }

    private void startTest() {
        Log.e("Test", "Calling package = "+ ActivityUtil.getCallingPackage(this));
        Log.e("Test", "Calling package 2= "+getCallingPackage());
        Log.e("Test", "Calling package 3= "+ Binder.getCallingUid());
        Log.e("Test", "Calling package 4= "+ getPackageManager().getNameForUid(Binder.getCallingUid()));
        AndroidUtil.getPermissionInfo(this);

        MathUtils.formatFloat();
    }
}
