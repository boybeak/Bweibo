package com.beak.bweibo.manager;

import android.content.Context;

import com.beak.bweibo.Sina;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

/**
 * Created by gaoyunfei on 15/5/10.
 */
public class ApiManager extends BaseManager {

    private static ApiManager sManager = null;

    public static synchronized ApiManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new ApiManager(context.getApplicationContext());
        }
        return sManager;
    }

    private static StatusesAPI sStatusesApi = null;
    private static UsersAPI sUserApi = null;
    private static CommentsAPI sCommentsApi = null;

    public ApiManager(Context context) {
        super(context);
    }

    public synchronized StatusesAPI getStatusesApiInstance () {
        if (sStatusesApi == null) {
            sStatusesApi = new StatusesAPI(getContext(), Sina.APP_KEY, OauthTokenManager.getInstance(getContext()).getToken());
        }
        return sStatusesApi;
    }

    public synchronized UsersAPI getUserApiInstance() {
        if (sUserApi == null) {
            sUserApi = new UsersAPI(getContext(), Sina.APP_KEY, OauthTokenManager.getInstance(getContext()).getToken());
        }
        return sUserApi;
    }

    public synchronized CommentsAPI getCommentsApiInstance () {
        if (sCommentsApi == null) {
            sCommentsApi = new CommentsAPI(getContext(), Sina.APP_KEY, OauthTokenManager.getInstance(getContext()).getToken());
        }
        return  sCommentsApi;
    }
}
