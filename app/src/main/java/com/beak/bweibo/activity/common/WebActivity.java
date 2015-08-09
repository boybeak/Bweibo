package com.beak.bweibo.activity.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.activity.ToolbarActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WebActivity extends ToolbarActivity {

    private static final String TAG = WebActivity.class.getSimpleName();

    private WebViewFragment mWebFragment = null;
    private WebView mWebView = null;

    @InjectView(R.id.web_menu_back) ImageView mBackBtn;
    @InjectView(R.id.web_menu_forward) ImageView mForwardBtn;
    @InjectView(R.id.web_menu_reload) ImageView mReloadBtn;
    @InjectView(R.id.web_progressbar) ProgressBar mProgressbar = null;

    private boolean isLoading = false;

    private WebViewClient mClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mReloadBtn.setImageResource(R.drawable.ic_window_close);
            mProgressbar.setVisibility(View.VISIBLE);
            isLoading = true;
            mBackBtn.setEnabled(mWebView.canGoBack());
            mForwardBtn.setEnabled(mWebView.canGoForward());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mReloadBtn.setImageResource(R.drawable.ic_reload);
            mProgressbar.setVisibility(View.GONE);
            isLoading = false;
        }



    };

    private WebChromeClient mChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            getToolbar().setTitle(view.getTitle());
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.inject(this);

        mWebFragment = (WebViewFragment)getFragmentManager().findFragmentById(R.id.web_fragment);
        mWebView = mWebFragment.getWebView();
        final String url = getIntent().getStringExtra(Finals.KEY_URL);

        mWebView.setWebChromeClient(mChromeClient);

        mWebView.setWebViewClient(mClient);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.loadUrl(url);

        /*AssetManager assetManager = getAssets();
        try {
            //AssetFileDescriptor fileDescriptor = assetManager.openNonAssetFd("html" + File.separator + "load_failed.html");
            InputStreamReader isr = new InputStreamReader(assetManager.open("html" + File.separator + "load_failed.html"));
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuilder htmlBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                htmlBuilder.append(line);
            }
            br.close();
            Log.v(TAG, "html=" + htmlBuilder);
            mWebView.loadData (htmlBuilder.toString(), "text/html", "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @OnClick({R.id.web_menu_back, R.id.web_menu_forward, R.id.web_menu_reload})
    protected void onClick (View view) {
        switch (view.getId()) {
            case R.id.web_menu_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                break;
            case R.id.web_menu_forward:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                break;
            case R.id.web_menu_reload:
                if (isLoading) {
                    mWebView.stopLoading();
                } else {
                    mWebView.reload();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.web_copy_url:
                ClipboardManager clipboardManager = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.getPrimaryClip().addItem(new ClipData.Item(mWebView.getUrl()));
                Toast.makeText(this, R.string.toast_web_copy_url_success, Toast.LENGTH_SHORT).show();
                break;
            case R.id.web_to_browser:
                ActivityDispatcher.browser(this, mWebView.getUrl());
                break;
            case R.id.web_share:
                ActivityDispatcher.shareText(this, mWebView.getUrl());
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
