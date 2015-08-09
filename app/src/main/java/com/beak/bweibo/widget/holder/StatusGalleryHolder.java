package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.beak.bweibo.R;
import com.beak.bweibo.widget.adapter.ThumbnailAdapter;
import com.beak.bweibo.widget.delegate.StatusDelegate;

/**
 * Created by gaoyunfei on 15/6/24.
 */
public class StatusGalleryHolder extends StatusHolder {

    public RecyclerView galleryRv = null;
    private StaggeredGridLayoutManager mLayoutManager = null;
    private ThumbnailAdapter mAdapter = null;

    public StatusGalleryHolder(View itemView) {
        super(itemView);
        galleryRv = (RecyclerView)itemView.findViewById(R.id.status_image_gallery);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        galleryRv.setLayoutManager(mLayoutManager);
    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        //if (mAdapter == null) {
            mAdapter = new ThumbnailAdapter(context, delegate.getSource().pic_urls);
        //}
        galleryRv.setAdapter(mAdapter);
    }
}
