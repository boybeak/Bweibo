package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.StatusDelegate;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class StatusMultiImageRepostHolder extends StatusRepostHolder {

    public GridLayout repostMultiImageGridLayout = null;

    public StatusMultiImageRepostHolder(View itemView) {
        super(itemView);
        repostMultiImageGridLayout = (GridLayout)itemView.findViewById(R.id.status_multi_image_repost_part);
    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        fillStatusImageGridPart(context, repostMultiImageGridLayout, delegate.getSource().getRetweeted_status().getPic_urls());
    }
}
