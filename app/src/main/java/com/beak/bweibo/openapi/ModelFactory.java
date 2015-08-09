package com.beak.bweibo.openapi;

import android.content.Context;
import android.text.TextUtils;

import com.beak.bweibo.R;
import com.beak.bweibo.manager.UserManager;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gaoyunfei on 15/6/15.
 */
public class ModelFactory {
    public static Status newStatus (Context context, String content, String path) {
        Status status = new Status();
        status.text = content;
        status.user = UserManager.getInstance(context).getMySelf();
        status.source = "<a href=\"http://weibo.com\" rel=\"nofollow\">" + context.getString(R.string.app_name) + "</a>";
        status.created_at = new Date();
        if (!TextUtils.isEmpty(path)) {
            ArrayList<Thumbnail> list = new ArrayList<Thumbnail>();
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setThumbnail_pic(path);
            list.add(thumbnail);
            status.pic_urls = list;
        }
        return status;
    }

    public static Status newStatus (Context context, String content, Status oldStatus) {
        Status status = newStatus(context, content, "");
        status.retweeted_status = oldStatus;
        return status;
    }
}
