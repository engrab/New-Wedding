package com.msint.weddingplanner.appBase;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context context;
    private static MyApp mInstance;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;
    }

    public static synchronized MyApp getInstance() {
        MyApp myApp;
        synchronized (MyApp.class) {
            myApp = mInstance;
        }
        return myApp;
    }
}
