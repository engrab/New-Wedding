package com.example.weddingplanner.all;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {
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
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
