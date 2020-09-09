package com.android.hq.androidutils.views.banner;

import android.content.Context;
import android.view.View;

public interface IViewHolder<T> {
    View createView(Context context);
    void onBind(Context context, int position, T data);
}
