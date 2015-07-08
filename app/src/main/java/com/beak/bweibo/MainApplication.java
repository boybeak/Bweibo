package com.beak.bweibo;

import android.app.Application;

import com.beak.beakkit.debug.Debug;

/**
 * Created by gaoyunfei on 15/4/22.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Debug.setDebugMode(true);
    }
}
