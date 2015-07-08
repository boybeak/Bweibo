package com.beak.bweibo;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

import java.io.Serializable;

/**
 * Created by gaoyunfei on 15/5/12.
 */
public class Result<T extends Serializable> {

    public boolean success;
    public T data;
    public ErrorInfo errorInfo;

    public static Result okResult = new Result(true, "", "");

    public Result (boolean scs, T t, WeiboException e) {
        this (scs, t, e.getLocalizedMessage());
    }

    public Result (boolean scs, T t, String erMsg) {
        this (scs, t, ErrorInfo.parse(erMsg));
    }

    public Result (boolean scs, T t, ErrorInfo info) {
        success = scs;
        data = t;
        errorInfo = info;
    }
}
