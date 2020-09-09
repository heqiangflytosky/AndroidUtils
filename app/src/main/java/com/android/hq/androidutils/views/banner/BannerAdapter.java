package com.android.hq.androidutils.views.banner;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BannerAdapter extends PagerAdapter {
    private SparseArray<TextView> mViewList = new SparseArray<>();
    private ViewPager mViewPager;
    private IViewHolder IViewHolder;
    private boolean mIsLoop;

    public BannerAdapter (ViewPager viewPager,/* IViewHolder holder, */boolean isLoop) {
        mViewPager = viewPager;
        mIsLoop = isLoop;
        //IViewHolder = holder;
    }

    @Override
    public int getCount() {
        return mIsLoop ? getRealCount() * 1: getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TextView item = mViewList.get(position);
        if (item == null) {
            item = new TextView(container.getContext());
            Log.e("Test", "添加第 " + position + " 个");
            item.setText("第 " + position + " 个");
            mViewList.append(position,item);
        }
        Log.e("Test", "显示第 " + position + " 个");
        container.addView(item);
        return item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        Log.e("Test", "删除第 " + position + " 个");
    }

//    @Override
//    public void finishUpdate(ViewGroup container) {
//        if(mIsLoop){
//            int position = mViewPager.getCurrentItem();
//            if (position == getCount() - 1) {
//                position = 0;
//                mViewPager.setCurrentItem(position);
//            }
//        }
//
//    }

    public int getRealCount() {
        return 5;
    }
}
