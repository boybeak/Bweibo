package com.beak.bweibo.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beak.bweibo.AutoDraftListener;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.ResultCallback;
import com.beak.bweibo.openapi.ModelFactory;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/12.
 */
public class StatusManager extends BaseManager {

    private static final String TAG = StatusManager.class.getSimpleName();

    public static final int
        COMMENT_TYPE_NONE = 0,
        COMMENT_TYPE_TO_CURRENT = 1,
        COMMENT_TYPE_TO_ORIGINAL = 2,
        COMMENT_TYPE_BOTH = 3;

    public static StatusManager sManager = null;

    public static synchronized StatusManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new StatusManager(context.getApplicationContext());
        }
        return sManager;
    }

    private List<StatusCallback> mStatusCallbackList = new ArrayList<StatusCallback>();

    private StatusManager(Context context) {
        super(context);
    }

    public void publishStatus (final String content, final ResultCallback<Status> callback) {
        //TODO
        Status status = ModelFactory.newStatus(getContext(), content, "");
        final String callbackId = requestOneId();
        notifyStatusCallbackCreate(callbackId, status);
        StatusRequestListener statusRequestListener = new StatusRequestListener(getContext()) {
            @Override
            public void onResult(Result<Status> result) {
                super.onResult(result);

                notifyStatusCallbackPublished(callbackId, result);

                callback.onResult(result);
            }
        };
        statusRequestListener.setContent(content);
        ApiManager.getInstance(getContext()).getStatusesApiInstance().update(content, "0", "0", statusRequestListener);
    }

    public void uploadStatus (String content , List<String> paths, final ResultCallback callback) {

        Status status = ModelFactory.newStatus(getContext(), content, paths.get(0));
        final String callbackId = requestOneId();
        notifyStatusCallbackCreate(callbackId, status);

        Bitmap bmp = BitmapFactory.decodeFile(paths.get(0));
        StatusRequestListener statusRequestListener = new StatusRequestListener(getContext()) {
            @Override
            public void onResult(Result<Status> result) {
                super.onResult(result);

                notifyStatusCallbackPublished(callbackId, result);

                callback.onResult(result);
            }
        };
        statusRequestListener.setContent(content);
        ApiManager.getInstance(getContext()).getStatusesApiInstance().upload(content, bmp, "0", "0", statusRequestListener);
    }

    public void friendTimeline (int index, final DefaultRequestListener<StatusList> listener) {
        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .friendsTimeline(0, 0, 15, index, false, 0, false, listener);
    }

    public void homeTimeline (/*int index*/long maxId, final DefaultRequestListener<StatusList> listener) {
        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .homeTimeline(0, maxId, 25, 1, false, 0, false, listener);
    }

    public void userTimeline (long uid, long maxId, final DefaultRequestListener<StatusList> listener) {
        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .userTimeline(uid, 0, maxId, 5, 1, false, 0, false, listener);
    }

    public void getStatusDetail (long id, DefaultRequestListener<Status> listener) {
        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .show(id, listener);
    }

    public void repostStatus (Status status, String appendText, int commentType, final ResultCallback<Status> callback) {
        Status newStatus = ModelFactory.newStatus(getContext(), appendText, status);
        final String callbackId = requestOneId();
        notifyStatusCallbackCreate(callbackId, newStatus);

        StatusRequestListener statusRequestListener = new StatusRequestListener(getContext()) {
            @Override
            public void onResult(Result<Status> result) {
                super.onResult(result);

                notifyStatusCallbackPublished(callbackId, result);

                callback.onResult(result);
            }
        };
        statusRequestListener.setContent(appendText);
        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .repost(status.id, appendText, commentType, statusRequestListener);
    }

    private void notifyStatusCallbackCreate (String callbackId, Status status) {
        if (mStatusCallbackList.isEmpty()) {
            return;
        }
        final int length = mStatusCallbackList.size();
        for (int i = 0; i < length; i++) {
            StatusCallback callback = mStatusCallbackList.get(i);
            callback.onStatusCreate(callbackId, status);
        }
    }

    private void notifyStatusCallbackPublished (String callbackId, Result<Status> result) {
        if (mStatusCallbackList.isEmpty()) {
            return;
        }
        final int length = mStatusCallbackList.size();
        for (int i = 0; i < length; i++) {
            StatusCallback callback = mStatusCallbackList.get(i);
            callback.onStatusPublished(callbackId, result);
        }
    }

    private void notifyStatusCallbackRemoved (String callbackId, Status status) {
        if (mStatusCallbackList.isEmpty()) {
            return;
        }
        final int length = mStatusCallbackList.size();
        for (int i = 0; i < length; i++) {
            StatusCallback callback = mStatusCallbackList.get(i);
            callback.onStatusRemoved(callbackId, status);
        }
    }

    public void registerStatusCallback (StatusCallback callback) {
        if (!mStatusCallbackList.contains(callback)) {
            mStatusCallbackList.add(callback);
        }
    }

    public void unregisterStatusCallback (StatusCallback callback) {
        if (mStatusCallbackList.contains(callback)) {
            mStatusCallbackList.remove(callback);
        }
    }

    public static abstract class StatusRequestListener extends AutoDraftListener<Status> {

        private String mContent = null;

        public StatusRequestListener (Context context) {
            this(context, false);
        }

        public StatusRequestListener(Context context, boolean debug) {
            super(context, debug, Status.class);
        }

    }

    public static interface StatusCallback {
        public void onStatusCreate (String callbackId, Status status);
        public void onStatusPublished (String callbackId, Result<Status> result);
        public void onStatusRemoved (String callbackId, Status status);
    }

}
