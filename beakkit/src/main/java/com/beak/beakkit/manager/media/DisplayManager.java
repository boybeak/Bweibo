package com.beak.beakkit.manager.media;

import android.content.Context;
import android.widget.ImageView;

import com.beak.beakkit.manager.AbsManager;
import com.beak.beakkit.widget.CircleAroundDisplayer;
import com.beak.beakkit.widget.SquareRoundDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by gaoyunfei on 15/5/20.
 */
public class DisplayManager extends AbsManager {

    private static final int CACHE_SIZE = 1024 * 1024 * 8;

    private static DisplayManager sManager = null;

    public synchronized static DisplayManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DisplayManager(context.getApplicationContext());
        }
        return sManager;
    }

    private ImageLoader mImageLoader = ImageLoader.getInstance();

    public DisplayManager(Context context) {
        super(context);
        if (!mImageLoader.isInited()) {
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                    .diskCacheSize(CACHE_SIZE)
                    .memoryCacheSize(CACHE_SIZE)
                    .build();
            mImageLoader.init(configuration);
        }
    }

    public ImageLoader getImageLoaderInstance () {
        return mImageLoader;
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
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        return options;
    }
}
