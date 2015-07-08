package com.beak.bweibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beak.beakkit.debug.Debug;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.fragment.StatusListFragment;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.manager.UserManager;
import com.beak.bweibo.manager.common.DraftManager;
import com.beak.bweibo.openapi.models.Draft;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.List;


public class MainActivity extends ToolbarActivity implements UserManager.OnMyselfPrepareCallback{

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewInject(R.id.main_drawer_layout)
    private DrawerLayout mMainDrawerLayout = null;

    private StatusListFragment mMainListFragment = null;

    private ActionBarDrawerToggle mToggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        mMainListFragment = (StatusListFragment)getSupportFragmentManager().findFragmentById(R.id.main_status_list_fragment);
        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainListFragment.smoothScrollTo(0);
            }
        });

        mToggle = new ActionBarDrawerToggle(this, mMainDrawerLayout, getToolbar(), R.string.app_name, R.string.hello_world);
        mMainDrawerLayout.setDrawerListener(mToggle);

        UserManager.getInstance(this).registerOnMyselfPrepareCallback(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mMainDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mMainDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (mMainDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mMainDrawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }
        if (this.getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            moveTaskToBack(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.getInstance(this).unregisterOnMyselfPreparedCallback(this);
    }

    @Override
    public void onPrepared(User mySelf) {
        getToolbar().setTitle(mySelf.name);
    }
}
