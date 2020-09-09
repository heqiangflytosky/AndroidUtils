package com.android.hq.androidutils;

import android.content.Intent;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.hq.androidutils.thememode.ThemeModeActivity;
import com.android.hq.androidutils.utils.ActivityUtil;
import com.android.hq.androidutils.utils.AndroidUtil;
import com.android.hq.androidutils.views.banner.BannerActivity;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://github.com/heqiangflytosky/AndroidUtils/");

        startTest();
    }

    private void startTest() {
        Log.e("Test", "Calling package = "+ActivityUtil.getCallingPackage(this));
        Log.e("Test", "Calling package 2= "+getCallingPackage());
        Log.e("Test", "Calling package 3= "+ Binder.getCallingUid());
        Log.e("Test", "Calling package 4= "+ getPackageManager().getNameForUid(Binder.getCallingUid()));
        AndroidUtil.getPermissionInfo(this);
        
        Intent intent = new Intent(this, BannerActivity.class);
        startActivity(intent);
    }
}
