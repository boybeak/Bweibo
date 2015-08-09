package com.beak.bweibo.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gaoyunfei on 15/7/29.
 */
public class MenuItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = null;

    public MenuItemDecoration () {
        mPaint = new Paint();
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        final int length = parent.getChildCount();
        for (int i = 0; i < length; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position < adapter.getItemCount() - 1) {
                final int left = child.getLeft();
                final int bottom = child.getBottom();
                final int right = child.getRight();
                c.drawLine(left, bottom, right, bottom, mPaint);
            }
        }
    }
}
