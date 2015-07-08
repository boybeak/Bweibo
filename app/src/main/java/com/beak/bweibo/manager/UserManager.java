package com.beak.bweibo.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.Result;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/28.
 */
public class UserManager extends BaseManager {

    private static final String TAG = UserManager.class.getSimpleName();

    private static final String KEY_MY_SELF_DB_NAME = "mySelf.db";

    private static UserManager sManager = null;

    public static synchronized UserManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new UserManager(context.getApplicationContext());
        }
        return sManager;
    }

    private List<OnMyselfPrepareCallback> mCallbackList = new ArrayList<OnMyselfPrepareCallback>();

    private User mMySelf = null;

    private UserManager(Context context) {
        super(context);
        try {
            readMySelfCache();
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "init mMySelf=" + mMySelf);
    }

    public void refreshMySelf() {
        String uidStr = OauthTokenManager.getInstance(getContext()).getToken().getUid();
        long uid = 0;
        try {
            uid = Long.parseLong(uidStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getUser(uid, new DefaultRequestListener<User>(getContext(), User.class) {
            @Override
            public void onResult(Result<User> result) {
                if (result.success) {
                    mMySelf = result.data;
                    try {
                        saveMySelfCache();
                    } catch (DbException e) {
                        e.printStackTrace();
                        Log.v(TAG, "refreshMySelf mMySelf=" + mMySelf);
                    }
                    final int length = mCallbackList.size();
                    for (int i = 0; i < length; i++) {
                        OnMyselfPrepareCallback callback = mCallbackList.get(i);
                        callback.onPrepared(mMySelf);
                    }
                }
            }
        });
    }

    private void saveMySelfCache () throws DbException {
        if (mMySelf != null) {
            DbUtils utils = DbUtils.create(getContext(), KEY_MY_SELF_DB_NAME);
            utils.createTableIfNotExist(User.class);
            utils.deleteAll(User.class);
            utils.save(mMySelf);
        }

    }

    private void readMySelfCache () throws DbException {
        DbUtils utils = DbUtils.create(getContext(), KEY_MY_SELF_DB_NAME);
        mMySelf = utils.findFirst(User.class);
    }

    public User getMySelf () {
        return mMySelf;
    }

    public void getUser (long uid, DefaultRequestListener<User> listener) {
        ApiManager.getInstance(getContext()).getUserApiInstance().show(uid, listener);
    }

    public void getUser (String screenName, DefaultRequestListener<User> listener) {
        ApiManager.getInstance(getContext()).getUserApiInstance().show(screenName, listener);
    }

    public void registerOnMyselfPrepareCallback (OnMyselfPrepareCallback callback) {
        if (!mCallbackList.contains(callback)) {
            mCallbackList.add(callback);
        }
    }

    public void unregisterOnMyselfPreparedCallback (OnMyselfPrepareCallback callback) {
        if (mCallbackList.contains(callback)) {
            mCallbackList.remove(callback);
        }
    }

    public static interface OnMyselfPrepareCallback {
        public void onPrepared (User mySelf);
    }
}
