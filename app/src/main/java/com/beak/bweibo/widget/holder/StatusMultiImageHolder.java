package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.StatusDelegate;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class StatusMultiImageHolder extends StatusHolder {

    public GridLayout imagesGridLayout = null;

    public StatusMultiImageHolder(View itemView) {
        super(itemView);
        imagesGridLayout = (GridLayout)itemView.findViewById(R.id.status_image_grid_part);
    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        fillStatusImageGridPart(context, imagesGridLayout, delegate.getSource().getPic_urls());
    }
}
