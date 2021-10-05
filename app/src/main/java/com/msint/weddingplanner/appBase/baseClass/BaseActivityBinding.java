package com.msint.weddingplanner.appBase.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivityBinding extends AppCompatActivity implements View.OnClickListener {
    public Context context;

    /* access modifiers changed from: protected */
    public abstract void initMethods();

    /* access modifiers changed from: protected */
    public abstract void setBinding();

    /* access modifiers changed from: protected */
    public abstract void setOnClicks();

    /* access modifiers changed from: protected */
    public abstract void setToolbar();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        setToolbar();
        setOnClicks();
        initMethods();
    }
}
