package com.android.hq.androidutils.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

/**
 *
 * @param <T>
 *     类似 AsyncTask
 *     配合在线程池中使用的 Task，耗时任务在子线程，结果在主线程执行
 *            AppExecutors.diskIO().execute(new AbstractTask<Boolean>() {
 *             @Override
 *             protected Boolean doInBackground() {
 *                 return null;
 *             }
 *
 *             @Override
 *             protected void onPostExecute(Boolean object) {
 *                 super.onPostExecute(object);
 *             }
 *         });
 */

public abstract class AbstractTask<T> implements Runnable {

    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    @WorkerThread
    protected abstract T doInBackground();

    @UiThread
    protected void onPostExecute(T object) {
    }

    @Override
    public final void run() {
        final T result = doInBackground();
        sMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(result);
            }
        });
    }
}