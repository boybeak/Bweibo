package com.beak.bweibo.widget.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beak.beakkit.utils.UiUtils;
import com.beak.bweibo.R;

/**
 * Created by gaoyunfei on 15/6/8.
 */
public class CommentDecoration extends RecyclerView.ItemDecoration {

    private int left, top, right, bottom, header;

    public CommentDecoration(Context context) {
        Resources resources = context.getResources();
        left = resources.getDimensionPixelSize(R.dimen.comment_item_margin_left);
        top = resources.getDimensionPixelSize(R.dimen.comment_item_margin_top);
        right = resources.getDimensionPixelSize(R.dimen.comment_item_margin_right);
        bottom = resources.getDimensionPixelSize(R.dimen.comment_item_margin_bottom);
        header = UiUtils.getActionBarHeight(context);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.set(left, top + header, right, bottom);
        } else {
            outRect.set(left, top, right, bottom);
        }
    }
}
