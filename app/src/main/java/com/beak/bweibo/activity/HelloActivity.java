package com.beak.bweibo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.beak.bweibo.R;
import com.beak.bweibo.Sina;
import com.beak.bweibo.manager.OauthTokenManager;
import com.beak.bweibo.openapi.SimpleOauthToken;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;


public class HelloActivity extends AppCompatActivity implements WeiboAuthListener {

    private static final String TAG = HelloActivity.class.getSimpleName();

    private TextView mLogTv = null;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        mLogTv = (TextView)findViewById(R.id.log_tv);

        Log.v(TAG, "is Local token ok " + OauthTokenManager.getInstance(this).getToken().isSessionValid());
        if (OauthTokenManager.getInstance(this).getToken().isSessionValid()) {
            Log.v(TAG, "token is ok and to MainActivity");
            startMain(3000);
        } else {
            startOauth();
        }

        //startMain();

    }

    private void startMain (long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(HelloActivity.this, MainActivity.class));
                finish();
            }
        }, delay);
    }

    private void startOauth () {
        AuthInfo info = new AuthInfo(this, Sina.APP_KEY, Sina.REDIRECT_URL, Sina.SCOPE);
        mSsoHandler = new SsoHandler(this, info);
        mSsoHandler.authorize(this);
        //mSsoHandler.authorizeClientSso(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult " + data.getData());

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onComplete(Bundle bundle) {
        mLogTv.setText("onComplete " + bundle);
        Log.v(TAG, "onComplete " + bundle);
        SimpleOauthToken token = new SimpleOauthToken(Oauth2AccessToken.parseAccessToken(bundle));
        OauthTokenManager.getInstance(this).saveOauthToken(token);
        startMain(1000);

    }

    @Override
    public void onWeiboException(WeiboException e) {
        mLogTv.setText("onWeiboException " + e.getLocalizedMessage());
        Log.e(TAG, "onWeiboException " + e.getLocalizedMessage());
    }

    @Override
    public void onCancel() {
        mLogTv.setText("onCancel ");
        Log.e(TAG, "onCancel ");
    }
}
