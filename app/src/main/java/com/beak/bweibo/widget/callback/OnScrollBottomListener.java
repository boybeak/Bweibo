package com.beak.bweibo.widget.callback;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by gaoyunfei on 15/5/29.
 */
public abstract class OnScrollBottomListener extends RecyclerView.OnScrollListener {

    private int mLastDy = 0;

    public OnScrollBottomListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalStateException("OnScrollBottomListener only user for RecyclerView with LinearLayoutManager");
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int pastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
            if ( (visibleItemCount + pastVisibleItem) >= totalItemCount && mLastDy > 0) {
                /*loadData(mIndex + 1);*/
                /*Log.v(TAG, "loading more happened " + (mIndex + 1));
                Debug.getInstance(getActivity()).d("loading more happened " + (mIndex + 1));*/
                onScrollBottom(recyclerView);
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mLastDy = dy;
    }

    public abstract void onScrollBottom (RecyclerView recyclerView);
}
