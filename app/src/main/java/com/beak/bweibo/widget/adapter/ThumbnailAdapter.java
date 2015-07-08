package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.R;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.List;

/**
 * Created by gaoyunfei on 15/6/24.
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailHolder> {

    private Context mContext = null;
    private List<Thumbnail> mThumbnails = null;

    public ThumbnailAdapter (Context context, List<Thumbnail> thumbnails) {
        mThumbnails = thumbnails;
        mContext = context;
    }

    @Override
    public ThumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ThumbnailHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_thumbnail, null));
    }

    @Override
    public void onBindViewHolder(ThumbnailHolder holder, int position) {
        Thumbnail thumbnail = mThumbnails.get(position);
        DisplayManager.getInstance(mContext)
                .getImageLoaderInstance()
                .displayImage(thumbnail.getThumbnail_pic(), holder.thumbnailIv);
    }

    @Override
    public int getItemCount() {
        return mThumbnails.size();
    }

    public class ThumbnailHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailIv = null;

        public ThumbnailHolder(View itemView) {
            super(itemView);
            thumbnailIv = (ImageView)itemView.findViewById(R.id.thumbnail_image);
        }
    }
}
