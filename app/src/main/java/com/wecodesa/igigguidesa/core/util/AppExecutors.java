package com.wecodesa.igigguidesa.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public class AppExecutors {

    private static AppExecutors instance;

    private final ExecutorService backgroundExecutor;
    private final Handler mainThreadHandler;

    private AppExecutors() {
        backgroundExecutor = Executors.newFixedThreadPool(3);
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    public void runOnBackground(Runnable runnable) {
        backgroundExecutor.execute(runnable);
    }

    public void runOnMainThread(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }
}