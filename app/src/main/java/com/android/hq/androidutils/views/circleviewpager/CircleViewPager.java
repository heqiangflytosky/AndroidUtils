package com.android.hq.androidutils.views.circleviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.hq.androidutils.R;

import java.util.ArrayList;

public class CircleViewPager extends LinearLayout {
    private Path mPath = new Path();
    private ViewPager2 mViewPager;
    private ViewPagerAdapter mAdapter;

    public CircleViewPager(Context context) {
        this(context,null);
    }

    public CircleViewPager(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CircleViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewPager = new ViewPager2(getContext());
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        addView(mViewPager);
        ArrayList<String> list = new ArrayList<>();
        list.add("第一页");
        list.add("第二页");
        list.add("第三页");
        mAdapter = new ViewPagerAdapter(list);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPath.reset();
        Log.e("Test","width = "+getWidth()+", height ="+getHeight());
        mPath.addCircle(getWidth() / 2 , getHeight() / 2, Math.min(getWidth() / 2 ,getHeight() / 2), Path.Direction.CW);
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
    }

    class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<String> mData;
        public ViewPagerAdapter(ArrayList<String> list) {
            mData = list;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new SimPleViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((SimPleViewHolder)viewHolder).textView.setText(mData.get(i));
        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }


    }

    public static class SimPleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SimPleViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_view_pager_item, parent, false));
            textView = itemView.findViewById(R.id.text);
        }
    }
}
