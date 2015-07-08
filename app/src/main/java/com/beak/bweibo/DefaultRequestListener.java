package com.beak.bweibo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.beak.beakkit.debug.Debug;
import com.beak.bweibo.manager.common.CacheDirManager;
import com.beak.bweibo.utils.DateHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by gaoyunfei on 15/5/15.
 */
public abstract class DefaultRequestListener <T extends Serializable> implements RequestListener {

    private static final String TAG = DefaultRequestListener.class.getSimpleName();

    private Context mContext = null;
    private boolean isDebugMode = false;
    private Class<T> mClz = null;

    public DefaultRequestListener (Context context, boolean debug, Class<T> clz) {
        mContext = context.getApplicationContext();
        isDebugMode = debug;
        mClz = clz;
    }

    public DefaultRequestListener (Context context, Class<T> clz) {
        this(context, false, clz);
    }

    @Override
    public void onComplete(String s) {
        if (isDebugMode) {
            Debug.getInstance(mContext).i(s);
            Log.i(TAG, "onComplete s=" + s);
        }
        try {
            Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss +SSSS yyyy").create();
            T t = gson.fromJson(s, mClz);
            Result<T> result = new Result<T>(true, t, ErrorInfo.getSuccessInfo());
            onResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            onWeiboException(new WeiboException(e));
            if (e instanceof MalformedJsonException) {
                manageMalformedJsonException(s, (MalformedJsonException)e);
            }
        }

    }

    @Override
    public void onWeiboException(WeiboException e) {
        if (isDebugMode) {
            Debug.getInstance(mContext).e(e.getLocalizedMessage());
            Log.e(TAG, "onWeiboException error=" + e.getLocalizedMessage());
        }

        //Gson gson = new Gson();
        //T t = gson.fromJson(s, new TypeToken<T>() {}.getType());
        Result<T> result = new Result<T>(false, null, ErrorInfo.getSuccessInfo());
        onResult(result);
    }

    private void manageMalformedJsonException (final String result, final MalformedJsonException e) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                File exceptionDir = CacheDirManager.getInstance(mContext).getExceptionCacheDir();
                File exceptionFile = new File(exceptionDir, e.getClass().getSimpleName() + "_" +DateHelper.FORMAT_C.format(new Date()) + ".exp");

                BufferedWriter bufferedWriter = null;
                try {
                    FileWriter fileWriter = new FileWriter(exceptionFile);
                    bufferedWriter = new BufferedWriter(fileWriter);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (bufferedWriter != null) {
                    try {
                        PrintWriter printWriter = new PrintWriter(bufferedWriter);
                        e.printStackTrace(printWriter);
                        bufferedWriter.write("\n" + result + "\n");
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public Context getContext () {
        return mContext;
    }

    public abstract void onResult (Result<T> result);
}
