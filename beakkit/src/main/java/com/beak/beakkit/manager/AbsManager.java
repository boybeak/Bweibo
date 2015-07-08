package com.beak.beakkit.manager;

import android.content.Context;

/**
 * Created by gaoyunfei on 15/5/20.
 */
public abstract class AbsManager {

    private Context mContext = null;

    public AbsManager(Context context) {
        mContext = context;
    }

    public Context getContext () {
        return mContext;
    }
}
