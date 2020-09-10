package com.android.hq.androidutils.views.banner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.SparseArray;
import android.view.ViewGroup;

public class BannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray mDataList;
    private boolean mIsLoop;

    public BannerAdapter (boolean isLoop, SparseArray dataList) {
        mIsLoop = isLoop;
        mDataList = dataList;
    }

    public int getRealCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder.SimPleViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder.SimPleViewHolder) {
            ((ViewHolder.SimPleViewHolder) holder).textView.setText("这是第"+position+"个");
            ((ViewHolder.SimPleViewHolder) holder).image.setImageURI(Uri.parse((String) mDataList.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return mIsLoop ? getRealCount() * 1: getRealCount();
    }
}
