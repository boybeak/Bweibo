package com.beak.bweibo.widget.delegate;

import com.beak.bweibo.utils.PatternUtils;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.List;

/**
 * Created by gaoyunfei on 15/5/22.
 */
public class StatusDelegate extends BaseDelegate<Status>{

    private boolean isClickable = true;
    private CharSequence mStatusSpannable = null;
    private CharSequence mRepostStatusSpannable = null;

    public StatusDelegate (Status status) {
        super(status);
        mStatusSpannable = PatternUtils.translate(status.text);
        if (status.hasRetweetOne()) {
            Status retweetOne = status.getRetweeted_status();
            if (retweetOne.user != null) {
                mRepostStatusSpannable = PatternUtils.translate("@" + retweetOne.user.name + ":" + retweetOne.text);
            } else {
                mRepostStatusSpannable = PatternUtils.translate(retweetOne.text);
            }
        }
    }

    public StatusDelegate (Status status, boolean isClickable) {
        this(status);
        setClickable(isClickable);
    }

    @Override
    public DelegateType getDelegateType() {
        Status status = getSource();
        if (status.getRetweeted_status() != null) {
            Status repostStatus = status.retweeted_status;
            List<Thumbnail> repostThumbnails = repostStatus.getPic_urls();
            if (repostThumbnails == null || repostThumbnails.isEmpty()) {
                return DelegateType.STATUS_TEXT_REPOST;
            } else {
                int size = repostThumbnails.size();
                if (size == 0) {
                    return DelegateType.STATUS_SINGLE_IMAGE_REPOST;
                } else {
                    return DelegateType.STATUS_MULTI_IMAGE_REPOST;
                }
            }
        }
        List<Thumbnail> thumbnails = status.getPic_urls();
        if (thumbnails == null || thumbnails.isEmpty()) {
            return DelegateType.STATUS_TEXT;
        } else {
            int size = thumbnails.size();
            if (size == 1) {
                return DelegateType.STATUS_SINGLE_IMAGE;
            } else {
//                if (size >= 5) {
//                    return DelegateType.STATUS_MULTI_IMAGE_GALLERY;
//                }
                return DelegateType.STATUS_MULTI_IMAGE;
            }
        }

//        return DelegateType.STATUS_TEXT;
    }

    public void setClickable (boolean isClickable) {
        this.isClickable = isClickable;
    }

    public boolean isClickable () {
        return isClickable;
    }

    public CharSequence getStatusSpannable() {
        return mStatusSpannable;
    }

    public CharSequence getRepostStatusSpannable() {
        return mRepostStatusSpannable;
    }
}
