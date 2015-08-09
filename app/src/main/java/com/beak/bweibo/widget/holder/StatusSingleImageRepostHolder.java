package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.List;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class StatusSingleImageRepostHolder extends StatusRepostHolder {

    public ImageView repostSingleImageIv = null;

    public StatusSingleImageRepostHolder(View itemView) {
        super(itemView);
        repostSingleImageIv = (ImageView)itemView.findViewById(R.id.status_single_image);
    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        Status source = delegate.getSource();
        rePostTextTv.setText(source.retweeted_status.text);
        List<Thumbnail> repostThums = source.retweeted_status.pic_urls;
        fillStatusImageViewPart(context, repostSingleImageIv, repostThums.get(0));
    }
}
