package com.ikotori.coolweather.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.socks.library.KLog;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Fashion at 2018/04/21 15:56.
 * Describe:
 */

public class AppExecutors {
    private static final int THREAD_COUNT = 5;

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT), new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
