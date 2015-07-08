package com.beak.bweibo;

import android.content.Context;
import android.util.Log;

import com.beak.bweibo.manager.common.DraftManager;
import com.beak.bweibo.openapi.models.Draft;

import java.io.Serializable;

public abstract class AutoDraftListener<T extends Serializable> extends DefaultRequestListener<T> {

    private static final String TAG = AutoDraftListener.class.getSimpleName();

    private String mContent = null;

    public AutoDraftListener(Context context, boolean debug, Class<T> clz) {
        super(context, debug, clz);
    }

    public AutoDraftListener(Context context, Class<T> clz) {
            super(context, clz);
        }

    @Override
    public void onResult(Result<T> result) {
        if (!result.success) {
            onException(mContent, result);
        }
    }

    public void setContent (String content) {
            mContent = content;
        }

    private void onException (String content, Result<T> result) {

        Draft draft = new Draft();
        draft.setContent(content);
        draft.setErrorMsg(result.errorInfo.error);
        boolean success = DraftManager.getInstance(this.getContext()).save(draft);
        Log.w(TAG, "onException save draft success=" + success);

    }
}