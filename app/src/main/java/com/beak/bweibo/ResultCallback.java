package com.beak.bweibo;

import java.io.Serializable;

/**
 * Created by gaoyunfei on 15/6/15.
 */
public interface ResultCallback<T extends Serializable> {
    public void onResult (Result<T> result);
}
