package com.beak.bweibo;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by gaoyunfei on 15/6/30.
 */
public class DefaultImageLoadingListener implements ImageLoadingListener {

    private static DefaultImageLoadingListener sListener = new DefaultImageLoadingListener();

    public static DefaultImageLoadingListener getInstance () {
        return sListener;
    }

    @Override
    public void onLoadingStarted(String s, View view) {
        if (view instanceof ImageView) {
            ((ImageView) view).setBackgroundResource(R.drawable.shape_profile_bg_empty);
        }
    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        if (view instanceof ImageView) {
            ((ImageView) view).setBackgroundDrawable(null);
        }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }
}
