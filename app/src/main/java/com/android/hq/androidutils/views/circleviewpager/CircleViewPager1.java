package com.android.hq.androidutils.views.circleviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.hq.androidutils.R;

import java.util.ArrayList;
import java.util.List;

public class CircleViewPager1 extends ViewPager {
    private List<View> mPagers = new ArrayList<>();
    private int mTotalOffset;
    private Path mPath = new Path();
    public CircleViewPager1(@NonNull Context context) {
        this(context,null);
    }

    public CircleViewPager1(@NonNull  Context context, @Nullable  AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(getContext(),R.layout.circle_view_pager_item, null);
        ((TextView) view.findViewById(R.id.text)).setText("第一页");
        View view2 = inflate(getContext(),R.layout.circle_view_pager_item, null);
        ((TextView) view2.findViewById(R.id.text)).setText("第二页");
        View view3 = inflate(getContext(),R.layout.circle_view_pager_item, null);
        ((TextView) view3.findViewById(R.id.text)).setText("第三页");
        addPage(view);
        addPage(view2);
        addPage(view3);
        setAdapter(mPagerAdapter);
        addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPath.reset();
        mPath.addCircle(getWidth()/2+mTotalOffset,getHeight()/2,Math.min(getWidth()/2,getHeight()/2),Path.Direction.CW);
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
    }

    public void addPage(View view) {
        mPagers.add(view);
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mPagers.get(position);
            if (view.getParent() != null) {
                container.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mPagers.get(position));
        }
    };

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mTotalOffset = getWidth() * position + positionOffsetPixels;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
