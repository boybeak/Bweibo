package com.beak.bweibo.manager;

import android.content.Context;

import com.beak.beakkit.manager.AbsManager;

import java.util.UUID;

/**
 * Created by gaoyunfei on 15/6/14.
 */
public abstract class BaseManager extends AbsManager {

    public BaseManager(Context context) {
        super(context);
    }

    public String requestOneId () {
        return UUID.randomUUID().toString();
    }
}
