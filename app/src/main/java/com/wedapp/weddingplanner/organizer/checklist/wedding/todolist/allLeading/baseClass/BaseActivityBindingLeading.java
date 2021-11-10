package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivityBindingLeading extends AppCompatActivity implements View.OnClickListener {
    public Context context;


    public abstract void initMethods();


    public abstract void setBinding();


    public abstract void setOnClicks();


    public abstract void setToolbar();


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        setToolbar();
        setOnClicks();
        initMethods();
    }
}
