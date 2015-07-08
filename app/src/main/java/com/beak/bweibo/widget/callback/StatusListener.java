package com.beak.bweibo.widget.callback;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by gaoyunfei on 15/6/13.
 */
public class StatusListener implements View.OnClickListener {

    private StatusDelegate mDelegate = null;

    public StatusListener (StatusDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onClick(View v) {
        Status status = mDelegate.getSource();
        User user = status.user;
        final Context context = v.getContext();
        switch (v.getId()) {
            case R.id.user_profile:
                ActivityDispatcher.userActivity(context, user);
                break;
            case R.id.user_publish_from:

                break;
            case R.id.control_support:
                break;
            case R.id.control_comment:
                ActivityDispatcher.commentActivity(context, status);
                break;
            case R.id.control_repost:
                ActivityDispatcher.repostActivity(context, status);
                break;
            case R.id.control_more:
                PopupMenu popupMenu = new PopupMenu(context, v, Gravity.LEFT);
                popupMenu.inflate(R.menu.menu_status_item);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.status_collect:
                                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                break;
            case R.id.status_text:
            case R.id.status_container:
                ActivityDispatcher.statusDetailActivity((Activity)context, v, status);
                break;
            case R.id.status_text_repost:
            case R.id.status_repost_container:
                Status retweetedStatus = status.retweeted_status;
                ActivityDispatcher.statusDetailActivity((Activity)context, v, retweetedStatus);
                break;
        }
    }
}
