package com.example.weddingplanner.allLeading.baseClass;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivityRecyclerBindingLeading extends AppCompatActivity implements View.OnClickListener {
    public Context context;


    public abstract void callApi();


    public abstract void fillData();


    public abstract void initMethods();


    public abstract void setBinding();


    public abstract void setOnClicks();


    public abstract void setRecycler();


    public abstract void setToolbar();


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
