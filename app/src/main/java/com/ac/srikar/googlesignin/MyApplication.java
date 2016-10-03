package com.ac.srikar.googlesignin;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Fresco Library
        Fresco.initialize(this);
    }
}
