package com.beak.bweibo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.beak.bweibo.R;

/**
 * Created by gaoyunfei on 15/5/9.
 */
public class ToolbarActivity extends AppCompatActivity {

    private Toolbar mToolbar = null;
    private FrameLayout mContentContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_toolbar);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mContentContainer = (FrameLayout)findViewById(R.id.content_container);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeAsUpEnable());
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null, true);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        mContentContainer.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentContainer.addView(view, params);
    }

    public Toolbar getToolbar () {
        return mToolbar;
    }

    public boolean isHomeAsUpEnable() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
