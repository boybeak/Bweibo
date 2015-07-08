package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beak.bweibo.R;

/**
 * Created by gaoyunfei on 15/6/6.
 */
public class StatusDecoration extends RecyclerView.ItemDecoration {

    private int left, top, right, bottom;

    public StatusDecoration (Context context) {
        Resources resources = context.getResources();
        left = resources.getDimensionPixelSize(R.dimen.status_item_margin_left);
        top = resources.getDimensionPixelSize(R.dimen.status_item_margin_top);
        right = resources.getDimensionPixelSize(R.dimen.status_item_margin_right);
        bottom = resources.getDimensionPixelSize(R.dimen.status_item_margin_bottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(left, top, right, bottom);
    }
}
