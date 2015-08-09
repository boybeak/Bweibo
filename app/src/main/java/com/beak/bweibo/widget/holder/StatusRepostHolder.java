package com.beak.bweibo.widget.holder;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.callback.StatusListener;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by gaoyunfei on 15/5/22.
 */
public class StatusRepostHolder extends StatusHolder {

    private static final String TAG = StatusRepostHolder.class.getSimpleName();

    public TextView rePostTextTv = null;

    public View repostView = null;

    private StatusListener mStatusListener = null;

    public StatusRepostHolder(View itemView) {
        super(itemView);
        rePostTextTv = (TextView)itemView.findViewById(R.id.status_text_repost);
        repostView = itemView.findViewById(R.id.status_repost_container);
    }

    @Override
    public void bindData(final Context context, StatusDelegate delegate) {
        super.bindData(context, delegate);
        mStatusListener = new StatusListener(delegate);
        final Status retweetedStatus = delegate.getSource().retweeted_status;
        /*User retweetedUser = retweetedStatus.user;
        if (retweetedUser != null) {*/
            rePostTextTv.setText(delegate.getRepostStatusSpannable());
            //rePostTextTv.setText("@" + retweetedUser.getName() + ":" + retweetedStatus.getText());
        /*} else {
            rePostTextTv.setText(retweetedStatus.getText());
        }*/
        rePostTextTv.setMovementMethod(LinkMovementMethod.getInstance());
        repostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rePostTextTv.setOnClickListener(mStatusListener);
        repostView.setOnClickListener(mStatusListener);
    }
}