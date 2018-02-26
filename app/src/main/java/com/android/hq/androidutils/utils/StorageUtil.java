package com.android.hq.androidutils.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by heqiang on 18-2-26.
 *
 * 获取可用存储空间大小
 * 获取总存储空间大小
 */

public class StorageUtil {
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }
}
