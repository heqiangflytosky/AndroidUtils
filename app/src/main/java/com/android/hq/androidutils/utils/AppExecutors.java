/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hq.androidutils.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Global executor pools for the whole application.
 * 提供后台线程以及主线程的执行接口
 */
public class AppExecutors {
    private static final int DEFAULT_CORE_THREAD_COUNT = 5;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.min(DEFAULT_CORE_THREAD_COUNT, CPU_COUNT / 2);
    private static final int MAX_POOL_SIZE_IO = 256;
    private static final int MAX_POOL_SIZE_COMPUTATION = CPU_COUNT * 2 + 1;

    private static final String THREAD_NAME_IO = "[io]-";
    private static final String THREAD_NAME_COMPUTATION = "[computation]-";

    private static final long KEEP_ALIVE_TIME = 30000L;

    private static final WorkQueue<Runnable> mIoWorkQueue = new WorkQueue<>();

    private static final RejectedExecutionHandler mIoRejectedPolicy = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            mIoWorkQueue.superOffer(r);
        }
    };

    private static final WorkQueue<Runnable> mComputationWorkQueue = new WorkQueue<>();

    private static final RejectedExecutionHandler mComputationRejectedPolicy = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            mComputationWorkQueue.superOffer(r);
        }
    };

    private static final Executor diskIO = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE_IO,
            KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, mIoWorkQueue,
            new DefaultThreadFactory(THREAD_NAME_IO), mIoRejectedPolicy);

    private static final Executor computation = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE_COMPUTATION,
            KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, mComputationWorkQueue,
            new DefaultThreadFactory(THREAD_NAME_COMPUTATION), mComputationRejectedPolicy);

    private static final Executor mainThread = new MainThreadExecutor();

    public static Executor diskIO() {
        return diskIO;
    }

    public static Executor computation() {
        return computation;
    }

    public static Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private final String namePrefix;

        DefaultThreadFactory(@NonNull String prefix) {
            namePrefix = prefix;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(r);
            t.setName(namePrefix + t.getId());
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            return t;
        }
    }

    private static class WorkQueue<E> extends LinkedBlockingQueue<E> {
        @Override
        public boolean offer(E e) {
            return false;
        }

        public void superOffer(E e) {
            super.offer(e);
        }
    }
}
