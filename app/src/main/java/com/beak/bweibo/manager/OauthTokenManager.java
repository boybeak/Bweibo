package com.beak.bweibo.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.beak.bweibo.openapi.SimpleOauthToken;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by gaoyunfei on 15/5/10.
 */
public class OauthTokenManager extends BaseManager {

    private static OauthTokenManager sManager = null;

    public static synchronized OauthTokenManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new OauthTokenManager(context.getApplicationContext());
        }
        return sManager;
    }

    private SimpleOauthToken mToken = null;

    private static final String
            TOKEN_PREFERENCE = "token_file",
            KEY_TOKEN = "token";

    private OauthTokenManager(Context context) {
        super(context);
        mToken = readOauthToken();
    }

    public SimpleOauthToken getToken () {
        return mToken;
    }

    public void saveOauthToken (SimpleOauthToken token) {
        mToken = token;
        String sharedName = TOKEN_PREFERENCE;
        SharedPreferences shared = getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(KEY_TOKEN, token.toJsonString());
        editor.commit();
    }

    public SimpleOauthToken readOauthToken () {
        String sharedName = TOKEN_PREFERENCE;
        SharedPreferences shared = getContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        String json = shared.getString(KEY_TOKEN, "");
        return new SimpleOauthToken(Oauth2AccessToken.parseAccessToken(json));
    }

}
