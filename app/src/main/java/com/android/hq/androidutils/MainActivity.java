package com.android.hq.androidutils;

import android.content.Intent;
import android.os.Binder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.hq.androidutils.utils.ActivityUtil;
import com.android.hq.androidutils.utils.AndroidUtil;
import com.android.hq.androidutils.views.banner.BannerActivity;
import com.android.hq.androidutils.views.seekbar.SeekBarActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mListAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void setData(){
        ArrayList<ListAdapter.DataBean> list = new ArrayList<>();
        list.add(new ListAdapter.DataBean(getResources().getString(R.string.title_common_test),
                getResources().getString(R.string.des_common_test), CommonTestActivity.class));
        list.add(new ListAdapter.DataBean(getResources().getString(R.string.title_banner),
                getResources().getString(R.string.des_banner), BannerActivity.class));
        list.add(new ListAdapter.DataBean(getResources().getString(R.string.title_seek_bar),
                getResources().getString(R.string.des_seek_bar), SeekBarActivity.class));

        mListAdapter.updateData(list);
    }
}
