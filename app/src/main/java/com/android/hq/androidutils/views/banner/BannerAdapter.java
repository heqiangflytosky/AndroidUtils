package com.android.hq.androidutils.views.banner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class BannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray mDataList;
    private OnItemClickListener mOnItemClickListener;
    private boolean mIsLoop;
    private int mExtrasCount = 2;

    public BannerAdapter (boolean isLoop) {
        mIsLoop = isLoop;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, convertRealPosition(position));
                }
            }
        });
        if (holder instanceof ViewHolder.SimPleViewHolder) {
            int realPosition = convertRealPosition(position);
            ((ViewHolder.SimPleViewHolder) holder).textView.setText("这是第"+realPosition+"个");
            ((ViewHolder.SimPleViewHolder) holder).image.setImageURI(Uri.parse((String) mDataList.get(realPosition)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mIsLoop ? getRealCount() + mExtrasCount: getRealCount();
    }

    public void setData(SparseArray data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public boolean shouldLoop() {
        return mIsLoop && getRealCount() > 1;
    }

    public void setExtraPageCount(int count) {
        mExtrasCount = count;
    }

    public int getExtraPageCount() {
        return mExtrasCount;
    }

    public int convertRealPosition(int position) {
        int realPosition = 0;
        if (getRealCount() > 1) {
            realPosition = (position - mExtrasCount/2) % getRealCount();
        }
        if (realPosition < 0) {
            realPosition += getRealCount();
        }
        return realPosition;
    }
}
