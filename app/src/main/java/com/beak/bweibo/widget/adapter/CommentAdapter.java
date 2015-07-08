package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.beak.bweibo.widget.delegate.AbsDelegate;
import com.beak.bweibo.widget.delegate.DelegateType;
import com.beak.bweibo.widget.holder.AbsViewHolder;
import com.beak.bweibo.widget.holder.CommentHolder;

/**
 * Created by gaoyunfei on 15/6/4.
 */
public class CommentAdapter extends StatusAdapter {

    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, DelegateType delegateType, View view) {
        if (delegateType == DelegateType.COMMENT_) {
            return new CommentHolder(view);
        }
        return super.onCreateAbsViewHolder(parent, viewType, delegateType, view);
    }
}
