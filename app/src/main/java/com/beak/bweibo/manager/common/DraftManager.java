package com.beak.bweibo.manager.common;

import android.content.Context;

import com.beak.bweibo.manager.BaseManager;
import com.beak.bweibo.manager.OauthTokenManager;
import com.beak.bweibo.openapi.models.Draft;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by gaoyunfei on 15/5/13.
 */
public class DraftManager extends BaseManager {

    private static final String TAG = DraftManager.class.getSimpleName();

    private static final String NAME_START_WITH = "draft_";

    private static DraftManager sManager = null;

    public static synchronized DraftManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new DraftManager(context.getApplicationContext());
        }
        return sManager;
    }

    private DbUtils mDbUtils = null;

    private List<Draft> mDraftList = null;

    public DraftManager(Context context) {
        super(context);
        String dbName = NAME_START_WITH + OauthTokenManager.getInstance(context).getToken().getUid();
        mDbUtils = DbUtils.create(context, dbName);
    }

    public boolean save (Draft errorStatus) {
        try {
            mDbUtils.save(errorStatus);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Draft> getDraftList() {
        try {
            return mDbUtils.findAll(Draft.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


}
