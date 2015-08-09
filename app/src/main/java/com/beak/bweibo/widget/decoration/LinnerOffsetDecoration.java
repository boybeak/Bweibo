package com.beak.bweibo.widget.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gaoyunfei on 15/7/28.
 */
public class LinnerOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mHeaderOffset, mFooterOffset, mLeftOffset, mTopOffset, mRightOffset, mBottomOffset;

    public LinnerOffsetDecoration (int headerOffset, int footerOffset, int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        mHeaderOffset = headerOffset;
        mFooterOffset = footerOffset;
        mLeftOffset = leftOffset;
        mTopOffset = topOffset;
        mRightOffset = rightOffset;
        mBottomOffset = bottomOffset;
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
            outRect.set(mLeftOffset, mHeaderOffset + mTopOffset, mRightOffset, mBottomOffset);
        } else if (position == adapter.getItemCount() - 1) {
            outRect.set(mLeftOffset, mTopOffset, mRightOffset, mFooterOffset + mBottomOffset);
        } else {
            outRect.set(mLeftOffset, mTopOffset, mRightOffset, mBottomOffset);
        }
    }
}
