package com.example.dailyhelper.utils;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomApplication extends Application {
    private ExecutorService singleThreadExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        // 스레드 풀 초기화
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public ExecutorService getSingleThreadExecutor() {
        return singleThreadExecutor;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (!singleThreadExecutor.isShutdown()) {
            singleThreadExecutor.shutdown();
        }
    }
}
