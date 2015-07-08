package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.utils.DateHelper;
import com.beak.bweibo.DefaultImageLoadingListener;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.callback.StatusListener;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;
import com.sina.weibo.sdk.openapi.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/16.
 */
public class StatusHolder extends AbsViewHolder<StatusDelegate> {

    public ImageView profileIv;

    public TextView nameTv;

    public TextView timeTv = null;

    public TextView fromTv = null;

    public TextView textTv = null;

    public TextView supportCountTv = null;

    public TextView commentCountTv = null;

    public TextView repostCountTv = null;

    public ImageView supportIv;

    public ImageView commentIv;

    public ImageView repostIv;

    public ImageView moreIv;

    private StatusListener mStatusListener = null;

    public StatusHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.user_profile);
        nameTv = (TextView)itemView.findViewById(R.id.user_name);
        textTv = (TextView)itemView.findViewById(R.id.status_text);
        timeTv = (TextView)itemView.findViewById(R.id.user_publish_time);
        fromTv = (TextView)itemView.findViewById(R.id.user_publish_from);

        supportCountTv = (TextView)itemView.findViewById(R.id.control_support_count);
        commentCountTv = (TextView)itemView.findViewById(R.id.control_comment_count);
        repostCountTv = (TextView)itemView.findViewById(R.id.control_repost_count);

        supportIv = (ImageView)itemView.findViewById(R.id.control_support);
        commentIv = (ImageView)itemView.findViewById(R.id.control_comment);
        repostIv = (ImageView)itemView.findViewById(R.id.control_repost);
        moreIv = (ImageView)itemView.findViewById(R.id.control_more);

    }

    @Override
    public void bindData(Context context, StatusDelegate delegate) {
        mStatusListener = new StatusListener(delegate);
        fillStatusUserPart(context, delegate);
        fillStatusTextPart(delegate);
        fillStatusCommonPart(context, delegate);
        fillViewClickListener(delegate);
    }

    private void fillViewClickListener (StatusDelegate delegate) {
        profileIv.setOnClickListener(mStatusListener);
        if (delegate.isClickable()) {
            textTv.setOnClickListener(mStatusListener);
        } else {
            textTv.setOnClickListener(null);
        }

        if (delegate.isClickable()) {
            this.itemView.setOnClickListener(mStatusListener);
        } else {
            this.itemView.setOnClickListener(null);
        }

        supportIv.setOnClickListener(mStatusListener);
        commentIv.setOnClickListener(mStatusListener);
        repostIv.setOnClickListener(mStatusListener);
        moreIv.setOnClickListener(mStatusListener);
    }

    private void fillStatusUserPart(final Context context, StatusDelegate delegate) {
        final Status status = delegate.getSource();
        final User user = status.user;
        profileIv.setImageDrawable(null);
        DisplayManager.getInstance(context)
                .displayRoundImage(
                        user.avatar_hd,
                        context.getResources().getDimensionPixelSize(R.dimen.profile_size_in_list),
                        profileIv,
                        DefaultImageLoadingListener.getInstance());
        /*DisplayManager.getInstance(context)
                .displayCircleImage(
                        user.avatar_hd,
                        context.getResources().getDimensionPixelSize(R.dimen.profile_size_in_list),
                        profileIv);*/

        nameTv.setText(user.screen_name);
        long now = System.currentTimeMillis();
        timeTv.setText(DateHelper.formatDateForStatus(context, status.created_at));
        fromTv.setText(Html.fromHtml(status.source).toString());

    }

    private void fillStatusTextPart(StatusDelegate delegate) {
        textTv.setText(delegate.getStatusSpannable());
        textTv.setMovementMethod(LinkMovementMethod.getInstance());

        //fromTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void fillStatusCommonPart(final Context context, final StatusDelegate delegate) {
        final Status status = delegate.getSource();

        supportCountTv.setText(formatCount(status.getAttitudes_count()));
        commentCountTv.setText(formatCount(status.getComments_count()));
        repostCountTv.setText(formatCount(status.getReposts_count()));

    }

    protected void fillStatusImageViewPart (final Context context, ImageView imageView, final Thumbnail thumbnail) {
        //Picasso.with(context).load(thumbnail.getThumbnail_pic()).into(imageView);
        DisplayManager.getInstance(context).getImageLoaderInstance().displayImage(
                thumbnail.getBmiddle_pic(), imageView,
                DisplayManager.getDefaultDisplayImageOptions(),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        ImageView iv = (ImageView) view;
                        iv.setImageDrawable(null);
                        view.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundHint));
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        view.setBackgroundDrawable(null);
                        ImageView iv = (ImageView) view;
                        float scale = (float) bitmap.getHeight() / bitmap.getWidth();
                        if (scale > 2 || scale < 0.5) {
                            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        } else {
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                }
        );
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.imagePreviewActivity(context, thumbnail);
            }
        });
    }

    protected void fillStatusImageGridPart(final Context context, GridLayout gridLayout, final ArrayList<Thumbnail> thumbnails) {
        final int childCount = gridLayout.getChildCount();
        final int size = thumbnails.size();
        final int length = Math.max(childCount, size);

        for (int i = 0; i < length; i++) {
            if (i < childCount && i < size) {
                String url = thumbnails.get(i).getBmiddle_pic();
                ImageView iv = (ImageView)gridLayout.getChildAt(i);
                iv.setImageDrawable(null);
                DisplayManager.getInstance(context)
                        .getImageLoaderInstance()
                        .displayImage(url, iv, DisplayManager.getDefaultDisplayImageOptions());
                iv.setVisibility(View.VISIBLE);
            } else if (i < childCount && i >= size) {
                gridLayout.getChildAt(i).setVisibility(View.GONE);
            } else if (i >= childCount && i < size) {
                ImageView iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_gallery_image_item, null);
                String url = thumbnails.get(i).getBmiddle_pic();
                DisplayManager.getInstance(context)
                        .getImageLoaderInstance()
                        .displayImage(url, iv, DisplayManager.getDefaultDisplayImageOptions());
                gridLayout.addView(iv);
            }
            gridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            final int index = i;
            gridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityDispatcher.imagePreviewActivity(context, thumbnails, index);
                }
            });
        }
    }

    private String formatCount (int count) {
        if (count == 0) {
            return "";
        }
        if (count > 1000) {
            return (float)(count * 10 / 1000) / 10 + "k";
        }
        return count + "";
    }

}
