package com.beak.bweibo.manager;

import android.content.Context;

import com.beak.bweibo.AutoDraftListener;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.Result;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class CommentManager extends BaseManager {

    private static final String TAG = CommentManager.class.getSimpleName();

    private static CommentManager sManager = null;
    public synchronized static CommentManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new CommentManager(context.getApplicationContext());
        }
        return sManager;
    }

    public CommentManager(Context context) {
        super(context);
    }

    public void getCommentList (long id, long maxId, DefaultRequestListener<CommentList> listener) {
        ApiManager.getInstance(getContext())
                .getCommentsApiInstance()
                .show(id, 0, maxId, 15, 1, 0, listener);
    }

    public void commentTo (Status status, String content, boolean alsoToOriginal, CommentRequestListener listener) {
        ApiManager.getInstance(getContext())
                .getCommentsApiInstance()
                .create(content, status.id, alsoToOriginal, listener);
    }

    public static class CommentRequestListener extends AutoDraftListener<Comment> {

        public CommentRequestListener(Context context, boolean debug) {
            super(context, debug, Comment.class);
        }

        public CommentRequestListener(Context context) {
            super(context, false, Comment.class);
        }

        @Override
        public void onResult(Result<Comment> result) {
            super.onResult(result);
        }
    }

}
