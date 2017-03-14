package com.android.hq.androidutils.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by heqiang on 17-3-10.
 *
 * 网络变化监听器
 * 初始化：NetWorkObserver.init(this);
 * 设置监听：NetWorkObserver.register(mNetworkListener);
 * 取消监听：NetWorkObserver.unRegister(mNetworkListener);
 */
public class NetWorkObserver {
    public interface NetworkListener {
        void onNetworkChanged(boolean connected, String type);
    }

    private static NetWorkObserver sInstance;
    private Context mAppContext;
    private WeakRefArrayList<NetworkListener> mListeners;
    private NetWorkChangeReceiver mReceiver;

    public static void init(Context context){
        if(sInstance == null){
            sInstance = new NetWorkObserver(context);
        }
    }

    private NetWorkObserver(Context context){
        mAppContext = context;
        mListeners = new WeakRefArrayList<>(20);
    }

    public static void register(NetworkListener l) {
        sInstance.registerListener(l);
    }

    public static void unRegister(NetworkListener l) {
        sInstance.unRegisterListener(l);
    }

    private void registerListener(NetworkListener l) {
        synchronized(mListeners) {
            if (mListeners.indexOf(l) < 0) {
                mListeners.add(l);
            }
        }
        if (mListeners.size() > 0 && mReceiver == null) {
            registerReceiver();
        }
    }

    private void unRegisterListener(NetworkListener l) {
        synchronized(mListeners) {
            mListeners.remove(l);
        }
        if (mListeners.size() <= 0 && mReceiver != null) {
            unregisterReceiver();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetWorkChangeReceiver();
        mAppContext.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        mAppContext.unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    public class NetWorkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNetOn = NetWorkUtil.isNetworkWorking(context);
            String type = NetWorkUtil.getNetworkType(context);
            synchronized(mListeners) {
                for (int i = 0; i < mListeners.size(); i++) {
                    NetworkListener l = mListeners.get(i);
                    if (l != null) {
                        l.onNetworkChanged(isNetOn, type);
                    }
                }
            }
        }
    }


    public static class WeakRefArrayList<E> {

        private final ArrayList<WeakReference<E>> mArrayList;

        public WeakRefArrayList(int size){
            mArrayList = new ArrayList<>(size);
        }

        public E get(int index) {
            WeakReference<E> o = mArrayList.get(index);
            return o != null ? o.get() : null;
        }

        public boolean add(E object) {
            if (object == null) {
                return false;
            }
            return mArrayList.add(new WeakReference<E>(object));
        }

        public E remove(int index) {
            WeakReference<E> o = mArrayList.remove(index);
            return o != null ? o.get() : null;
        }

        public boolean remove(Object object) {
            int index = indexOf(object);
            if (index >= 0 && index < mArrayList.size()) {
                remove(index);
                return true;
            }
            return false;
        }

        public int indexOf(Object object) {
            if (object == null) {
                return -1;
            }
            for (int i = 0; i < mArrayList.size(); i++) {
                WeakReference<E> o = mArrayList.get(i);
                if (o != null && object.equals(o.get())) {
                    return i;
                }
            }
            return -1;
        }

        public int size() {
            return mArrayList.size();
        }
    }
}
