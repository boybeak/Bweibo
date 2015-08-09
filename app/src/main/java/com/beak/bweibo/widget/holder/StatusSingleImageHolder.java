package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.List;

/**
 * Created by gaoyunfei on 15/5/22.
 */
public class StatusSingleImageHolder extends StatusHolder {

    private static final String TAG = StatusSingleImageHolder.class.getSimpleName();

    public ImageView singleImageIv = null;

    public StatusSingleImageHolder(View itemView) {
        super(itemView);
        singleImageIv = (ImageView)itemView.findViewById(R.id.status_single_image);
    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        Status singleImgStatus = delegate.getSource();
        List<Thumbnail> thumbnails = singleImgStatus.pic_urls;
        fillStatusImageViewPart(context, singleImageIv, thumbnails.get(0));
    }


}
