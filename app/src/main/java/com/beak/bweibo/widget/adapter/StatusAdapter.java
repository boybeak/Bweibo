package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.beak.bweibo.widget.delegate.AbsDelegate;
import com.beak.bweibo.widget.delegate.DelegateType;
import com.beak.bweibo.widget.holder.AbsViewHolder;
import com.beak.bweibo.widget.holder.StatusGalleryHolder;
import com.beak.bweibo.widget.holder.StatusHolder;
import com.beak.bweibo.widget.holder.StatusMultiImageHolder;
import com.beak.bweibo.widget.holder.StatusMultiImageRepostHolder;
import com.beak.bweibo.widget.holder.StatusRepostHolder;
import com.beak.bweibo.widget.holder.StatusSingleImageHolder;
import com.beak.bweibo.widget.holder.StatusSingleImageRepostHolder;

/**
 * Created by gaoyunfei on 15/6/4.
 */
public class StatusAdapter extends FooterAdapter {

    public StatusAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, DelegateType delegateType, View view) {
        DelegateType type = DelegateType.getDelegateTypeById(viewType);
        AbsViewHolder holder = null;
        switch (type) {
            case STATUS_TEXT:
                holder = new StatusHolder(view);
                break;
            case STATUS_SINGLE_IMAGE:
                holder = new StatusSingleImageHolder(view);
                break;
            case STATUS_MULTI_IMAGE:
                holder = new StatusMultiImageHolder(view);
                break;
            case STATUS_MULTI_IMAGE_GALLERY:
                holder = new StatusGalleryHolder(view);
                break;
            case STATUS_TEXT_REPOST:
                holder = new StatusRepostHolder(view);
                break;
            case STATUS_SINGLE_IMAGE_REPOST:
                holder = new StatusSingleImageRepostHolder(view);
                break;
            case STATUS_MULTI_IMAGE_REPOST:
                holder = new StatusMultiImageRepostHolder(view);
                break;
            default:
                return super.onCreateAbsViewHolder(parent, viewType, delegateType, view);
        }
        return holder;
    }
}
