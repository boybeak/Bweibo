package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.utils.DateHelper;
import com.beak.bweibo.DefaultImageLoadingListener;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.CommentDelegate;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class CommentHolder extends AbsViewHolder<CommentDelegate> {

    public ImageView profileIv = null;
    public TextView nameTv = null, timeTv = null, supportCountTv = null;
    public TextView commentTextTv = null;

    public CommentHolder(View itemView) {
        super(itemView);
        profileIv = (ImageView)itemView.findViewById(R.id.comment_profile);
        nameTv = (TextView)itemView.findViewById(R.id.comment_name);
        timeTv = (TextView)itemView.findViewById(R.id.comment_time);
        supportCountTv = (TextView)itemView.findViewById(R.id.comment_support_count);
        commentTextTv = (TextView)itemView.findViewById(R.id.comment_text);
    }

    @Override
    public void bindData(final Context context, CommentDelegate commentDelegate) {
        final Comment comment = commentDelegate.getSource();
        final User user = comment.user;
        DisplayManager.getInstance(context).displayRoundImage(
                user.avatar_hd,
                context.getResources().getDimensionPixelSize(R.dimen.profile_size_in_list),
                profileIv,
                DefaultImageLoadingListener.getInstance());
        nameTv.setText(user.name);
        timeTv.setText(DateHelper.formatDateForStatus(context, comment.created_at));
        supportCountTv.setText(comment.floor_num + "");
        commentTextTv.setText(commentDelegate.getSpnnable());
        commentTextTv.setMovementMethod(LinkMovementMethod.getInstance());
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.userActivity(context, user);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.commentActivity(context, comment);
            }
        };
        commentTextTv.setOnClickListener(listener);
        itemView.setOnClickListener(listener);
    }

}
