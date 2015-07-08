package com.beak.bweibo.openapi;

import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by gaoyunfei on 15/5/10.
 */
public class SimpleOauthToken extends Oauth2AccessToken{

    private Oauth2AccessToken mToken = null;

    public SimpleOauthToken (Oauth2AccessToken token) {
        mToken = token;
    }

    @Override
    public boolean isSessionValid() {
        return mToken != null && mToken.isSessionValid();
    }

    @Override
    public Bundle toBundle() {
        return mToken.toBundle();
    }

    @Override
    public String toString() {
        return mToken.toString();
    }

    @Override
    public String getUid() {
        return mToken.getUid();
    }

    @Override
    public void setUid(String uid) {
        mToken.setUid(uid);
    }

    @Override
    public String getToken() {
        return mToken.getToken();
    }

    @Override
    public void setToken(String mTokenStr) {
        mToken.setToken(mTokenStr);
    }

    @Override
    public String getRefreshToken() {
        return mToken.getRefreshToken();
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        mToken.setRefreshToken(refreshToken);
    }

    @Override
    public long getExpiresTime() {
        return mToken.getExpiresTime();
    }

    @Override
    public void setExpiresTime(long mExpiresTime) {
        mToken.setExpiresTime(mExpiresTime);
    }

    @Override
    public void setExpiresIn(String expiresIn) {
        mToken.setExpiresIn(expiresIn);
    }

    public String toJsonString () {
        return "{" + mToken.toString() + "}";
    }

}
