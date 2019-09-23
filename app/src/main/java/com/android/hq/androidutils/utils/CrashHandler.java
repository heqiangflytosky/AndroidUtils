package com.android.hq.androidutils.utils;

import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Created by heqiang on 16-10-26.
 *
 * 1.OutOfMemoryError 时生成.hprof文件可以用MAT来分析定位是否有内存泄漏等问题
 * 2.解决 FinalizerWatchdogDaemon 线程的 TimeoutException 问题
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    private String mPackageName;
    private int mPid;
    private StringBuilder mFilename = new StringBuilder(120);
    private final String OOM_TARGET_FOLDER = "/crash_log/";
    private static final long ONE_DAY_TIME_IN_MILLISECONDS = 24*60*60*1000;
    private boolean mDumpingHprof;

    CrashHandler(String pkgName, int pid) {
        this.mPackageName = pkgName;
        this.mPid = pid;
        this.mDumpingHprof = false;
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        // 处理 OOM 日志问题
        if(throwable instanceof OutOfMemoryError && !mDumpingHprof) {
            Log.w(TAG, "CrashHandler capture a oom exception !!!");
            if(!dumpHprofData()) {
                Log.e(TAG, "Aborting ...");
            }else{
                //uploadExceptionToServer(); // TODO
            }
        }

        // 忽略 FinalizerWatchdogDaemon 线程的 TimeoutException 问题
        if (thread != null && "FinalizerWatchdogDaemon".equals(thread.getName()) && throwable instanceof TimeoutException) {
            Log.e(TAG, "CrashHandler", throwable);
            return;
        }

        if(mDefaultUncaughtExceptionHandler != null){
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    //获取目录大小，单位 M
    private double getDirSize(File file) {
        if(!file.exists()) {
            Log.w(TAG, file.toString() + " may not exists !");
            return 0.0D;
        } else if(!file.isDirectory()) {
            double size = (double)file.length() / 1024.0D / 1024.0D;
            return size;
        } else {
            File[] files = file.listFiles();
            double size = 0.0D;
            int length = files.length;

            for(int i = 0; i < length; ++i) {
                File f = files[i];
                size += this.getDirSize(f);
            }

            return size;
        }
    }

    //当目录大于1G时且创建时间大于1天时删除旧文件
    private void deleteOldFiles(File file) {
        if(getDirSize(file) >= 1024.0D) {
            Log.w(TAG, "begin to delete old files !");
            long currentTimeMillis = System.currentTimeMillis();
            File[] listFiles = file.listFiles();

            for(int i = 0; i < listFiles.length; ++i) {
                if(listFiles[i].isFile()) {
                    if(currentTimeMillis - listFiles[i].lastModified() > ONE_DAY_TIME_IN_MILLISECONDS) {
                        listFiles[i].delete();
                    }
                } else if(listFiles[i].isDirectory()) {
                    deleteOldFiles(listFiles[i]);
                }
            }

        }
    }

    private boolean getDumpDestinationOfHeap() {
        mFilename.delete(0, mFilename.length());
        mFilename.append(Environment.getExternalStorageDirectory());
        mFilename.append(OOM_TARGET_FOLDER);

        File target = new File(mFilename.toString());
        if (!target.exists()) {
            if (!target.mkdirs()) {
                Log.e(TAG, "Creating target hprof directory: \"" + mFilename.toString() + "\" was failed!");
                return false;
            }
        }
        deleteOldFiles(target);
        mFilename.append(mPackageName);
        mFilename.append("_PID:" + mPid);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        mFilename.append("_TIME:"+time);
        mFilename.append(".hprof");
        target = new File(mFilename.toString().trim());
        try {
            target.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "Creating target hprof file: \"" + mFilename.toString() + "\" was failed! Reason:" + e);

            return false;
        }

        return true;
    }

    private boolean dumpHprofData() {
        if (!getDumpDestinationOfHeap()) {
            return false;
        }
        Log.w(TAG, "Begin to dump hprof to " + mFilename.toString());
        long beginDumpTime = SystemClock.uptimeMillis();
        try {
            mDumpingHprof = true;
            Debug.dumpHprofData(mFilename.toString());
        } catch (IOException e) {
            Log.e(TAG, "Dump hprof to " + mFilename.toString() + " failed ! " + e);
            return false;
        }
        long endDumpTime = (SystemClock.uptimeMillis() - beginDumpTime) / 1000;
        Log.w(TAG, "Dump succeed!, Took " + endDumpTime + "s");
        return true;
    }

    public static void registerExceptionHandler(Context context) {
        CrashHandler handler = new CrashHandler(context.getPackageName(), android.os.Process.myPid());
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
