package com.msint.weddingplanner.appBase.baseClass;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivityRecyclerBinding extends AppCompatActivity implements View.OnClickListener {
    public Context context;

    /* access modifiers changed from: protected */
    public abstract void callApi();

    /* access modifiers changed from: protected */
    public abstract void fillData();

    /* access modifiers changed from: protected */
    public abstract void initMethods();

    /* access modifiers changed from: protected */
    public abstract void setBinding();

    /* access modifiers changed from: protected */
    public abstract void setOnClicks();

    /* access modifiers changed from: protected */
    public abstract void setRecycler();

    /* access modifiers changed from: protected */
    public abstract void setToolbar();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        setToolbar();
        callApi();
        fillData();
        setRecycler();
        setOnClicks();
        initMethods();
    }
}
