package com.beak.beakkit.manager.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.beak.beakkit.R;
import com.beak.beakkit.manager.AbsManager;
import com.beak.beakkit.widget.CircleAroundDisplayer;
import com.beak.beakkit.widget.SquareRoundDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by gaoyunfei on 15/5/20.
 */
public class DisplayManager extends AbsManager {

    private static final int CACHE_SIZE = 1024 * 1024 * 24;

    private static DisplayImageOptions mDefaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    private static DisplayManager sManager = null;

    public synchronized static DisplayManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DisplayManager(context.getApplicationContext());
        }
        return sManager;
    }

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private ImageLoadingListener mDefaultListener = null;

    private int mDefaultHintColor = 0;

    public DisplayManager(Context context) {
        super(context);
        if (!mImageLoader.isInited()) {
            mDefaultHintColor = context.getResources().getColor(R.color.defaultColorHint);
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                    .diskCacheSize(CACHE_SIZE)
                    .memoryCacheSize(CACHE_SIZE)
                    .defaultDisplayImageOptions(mDefaultOptions)
                    .threadPoolSize(8)
                    .threadPriority(Thread.MIN_PRIORITY)
                    .build();
            mDefaultListener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    view.setBackgroundColor(mDefaultHintColor);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    view.setBackgroundDrawable(null);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            };
            mImageLoader.setDefaultLoadingListener(mDefaultListener);
            mImageLoader.init(configuration);
        }
    }

    public ImageLoader getImageLoaderInstance () {
        return mImageLoader;
    }

    public void displayImage (String uri, ImageView iv) {
        mImageLoader.displayImage(uri, iv);
    }

    public void displayImage (String uri, ImageView iv, DisplayImageOptions options) {
        mImageLoader.displayImage(uri, iv, options);
    }

    public void displayImage (String uri, ImageView iv, ImageLoadingListener listener) {
        mImageLoader.displayImage(uri, iv, listener);
    }

    public void displayImage (String uri, ImageView iv, DisplayImageOptions options, ImageLoadingListener listener) {
        mImageLoader.displayImage(uri, iv, options, listener);
    }

    public void displayImage (String uri, ImageView iv, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener loadingProgressListener) {
        mImageLoader.displayImage(uri, iv, options, listener, loadingProgressListener);
    }

    public void displayRoundImage (String uri, int size, ImageView iv, ImageLoadingListener listener) {
        mImageLoader.displayImage(uri, iv, getRoundDisplayImageOptions(size), listener);
    }

    public void displayCircleImage (String uri, int size, ImageView iv) {
        mImageLoader.displayImage(uri, iv, getCircleDisplayImageOptions(size));
    }

    public static DisplayImageOptions getRoundDisplayImageOptions(int size) {
        return getRoundDisplayImageOptions(size, size / 2);
    }

    public static DisplayImageOptions getCircleDisplayImageOptions  (int size) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new CircleAroundDisplayer(size))
                .build();
        return options;
    }

    public static DisplayImageOptions getRoundDisplayImageOptions(int size, int round) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new SquareRoundDisplayer(size, round))
                .build();
        return options;
    }

    public static DisplayImageOptions getDefaultDisplayImageOptions () {
        return mDefaultOptions;
    }

    public int getDefaultHintColor () {
        return mDefaultHintColor;
    }
}
