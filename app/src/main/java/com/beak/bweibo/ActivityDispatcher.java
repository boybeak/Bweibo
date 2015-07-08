package com.beak.bweibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.beak.bweibo.activity.PublishActivity;
import com.beak.bweibo.activity.StatusDetailActivity;
import com.beak.bweibo.activity.TopicActivity;
import com.beak.bweibo.activity.UserActivity;
import com.beak.bweibo.activity.common.ImagePreviewActivity;
import com.beak.bweibo.activity.common.ImageSelectActivity;
import com.beak.bweibo.activity.common.WebActivity;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class ActivityDispatcher {
    public static void statusDetailActivity (Activity activity, View sharedView, Status status) {
        Intent it = new Intent(activity, StatusDetailActivity.class);
        it.putExtra(Finals.KEY_STATUS, status);
//        ActivityOptionsCompat optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, "A");
//        ActivityCompat.startActivity(activity, it, optionsCompat.toBundle());
        activity.startActivity(it);

    }

    public static void imagePreviewActivity (Context context, ArrayList<Thumbnail> thumbnails, int index) {
        Intent it = new Intent(context, ImagePreviewActivity.class);
        it.putParcelableArrayListExtra(Finals.KEY_IMAGE_LIST, thumbnails);
        it.putExtra(Finals.KEY_INDEX, index);
        context.startActivity(it);
    }

    public static void imagePreviewActivity (Context context, Thumbnail thumbnail) {
        ArrayList<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
        thumbnails.add(thumbnail);
        imagePreviewActivity(context, thumbnails, 0);
    }

    public static void imageSelectActivity (Context context) {
        Intent it = new Intent(context, ImageSelectActivity.class);
        context.startActivity(it);
    }

    public static void userActivity (Context context, long uid) {
        Intent it = new Intent (context, UserActivity.class);
        it.putExtra(Finals.KEY_UID, uid);
        context.startActivity(it);
    }
    public static void userActivity (Context context, User uid) {
        Intent it = new Intent (context, UserActivity.class);
        it.putExtra(Finals.KEY_USER, uid);
        context.startActivity(it);
    }

    public static void browser (Context context, String url) {
        try {
            Intent it = new Intent(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            it.setData(content_url);
            context.startActivity(it);
        } catch (Exception e) {
            Toast.makeText(context, R.string.toast_no_application_found_to_handle_this_intent, Toast.LENGTH_SHORT).show ();
        }

    }

    public static void innerBrowser (Context context, String url) {
        Intent it = new Intent(context, WebActivity.class);
        it.putExtra(Finals.KEY_URL, url);
        context.startActivity(it);

    }

    public static void shareText (Context context, String text) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(context, R.string.toast_no_application_found_to_handle_this_intent, Toast.LENGTH_SHORT).show ();
        }

    }

    public static void userActivity(Context context, String at) {
        Intent it = new Intent(context, UserActivity.class);
        it.putExtra(Finals.KEY_AT, at);
        context.startActivity(it);
    }

    public static void topicActivity (Context context, String topic) {
        Intent it = new Intent (context, TopicActivity.class);
        it.putExtra(Finals.KEY_TOPIC, topic);
        context.startActivity(it);
    }

    public static void commentActivity (Context context, Status status) {
        Intent it = new Intent(context, PublishActivity.class);
        it.putExtra(Finals.KEY_PUBLISH_MODE, PublishActivity.PUBLISH_MODE_COMMENT);
        it.putExtra(Finals.KEY_STATUS, status);
        context.startActivity(it);
    }

    public static void repostActivity (Context context, Status status) {
        Intent it = new Intent(context, PublishActivity.class);
        it.putExtra(Finals.KEY_STATUS, status);
        it.putExtra(Finals.KEY_PUBLISH_MODE, PublishActivity.PUBLISH_MODE_REPOST);
        context.startActivity(it);
    }
}
