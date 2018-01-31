package com.android.hq.androidutils.utils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by heqiang on 18-1-31.
 * 简单LRU算法实现缓存大小的限制策略
 */

public class FileCacheManager {

    private final Map<File, Long> mLastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
    private final AtomicInteger mCacheSize;
    private final int SIZE_LIMIT = 10 * 1024 * 1024;
    private final String mFilePath;

    private FileCacheManager() {
        mFilePath = AppContextUtils.getAppContext().getExternalCacheDir().getAbsolutePath();
        mCacheSize = new AtomicInteger();
        calculateCacheSizeAndFillUsageMap();
    }

    private void calculateCacheSizeAndFillUsageMap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = 0;
                File imageDir = new File(mFilePath);
                File[] cachedFiles = imageDir.listFiles();
                if(cachedFiles != null){
                    for(File cachedFile : cachedFiles){
                        size += cachedFile.length();
                        mLastUsageDates.put(cachedFile, cachedFile.lastModified());
                    }
                    mCacheSize.set(size);
                }
            }
        }).start();
    }


    private void putFile(String path){
        if(path == null)
            return;
        File file = new File(path);
        if(!file.exists())
            return;
        int valueSize = (int)file.length();
        int curCacheSize = mCacheSize.get();
        while (curCacheSize + valueSize > SIZE_LIMIT) {
            int freedSize = removeMostLongUsedFile();
            if (freedSize == -1)
                break;
            curCacheSize = mCacheSize.addAndGet(-freedSize);
        }
        mCacheSize.addAndGet(valueSize);
        mLastUsageDates.put(file, file.lastModified());
    }

    private int removeMostLongUsedFile(){
        if (mLastUsageDates.isEmpty()) {
            return -1;
        }
        Long oldestUsage = null;
        File mostLongUsedFile = null;
        Set<Map.Entry<File, Long>> entries = mLastUsageDates.entrySet();
        synchronized (mLastUsageDates) {
            for (Map.Entry<File, Long> entry : entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = entry.getKey();
                    oldestUsage = entry.getValue();
                } else {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage;
                        mostLongUsedFile = entry.getKey();
                    }
                }
            }
        }

        int fileSize = 0;
        if (mostLongUsedFile != null) {
            if (mostLongUsedFile.exists()) {
                fileSize = (int)mostLongUsedFile.length();
                if (mostLongUsedFile.delete()) {
                    mLastUsageDates.remove(mostLongUsedFile);
                }
            } else {
                mLastUsageDates.remove(mostLongUsedFile);
            }
        }
        return fileSize;
    }

    public void saveFile(String name) {
        putFile(name);
    }
}
