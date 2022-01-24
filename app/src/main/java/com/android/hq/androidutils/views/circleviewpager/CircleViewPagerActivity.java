package com.android.hq.androidutils.views.circleviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.SparseArray;

import com.android.hq.androidutils.R;

import java.util.ArrayList;

public class CircleViewPagerActivity extends AppCompatActivity {

//    private ViewPager2 mViewPager;
//    private ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_view_pager);
//        mViewPager = findViewById(R.id.view_pager);
//        ArrayList<String> list = new ArrayList<>();
//        list.add("第一页");
//        list.add("第二页");
//        list.add("第三页");
//        mAdapter = new ViewPagerAdapter(list);
//        mViewPager.setAdapter(mAdapter);
    }
}