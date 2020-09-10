package com.android.hq.androidutils.views.banner;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.hq.androidutils.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ViewHolder {
    public static class SimPleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public SimpleDraweeView image;
        public SimPleViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_banner_item, parent, false));
            textView = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
        }
    }
}
