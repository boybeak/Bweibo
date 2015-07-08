package com.beak.bweibo.openapi.models;

/**
 * Created by gaoyunfei on 15/5/28.
 */
public class FooterState {
    public static final int STATE_SUCCESS = 0, STATE_FAILED = 1, STATE_LOADING = 3, STATE_NONE = -1;

    private int mState = STATE_SUCCESS;

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }
}
