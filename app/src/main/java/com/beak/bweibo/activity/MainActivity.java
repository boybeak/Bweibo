package com.beak.bweibo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.beak.bweibo.R;
import com.beak.bweibo.fragment.MySelfFragment;
import com.beak.bweibo.fragment.StatusListFragment;
import com.beak.bweibo.manager.UserManager;
import com.beak.bweibo.manager.common.CacheDirManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.weibo.sdk.openapi.models.User;

import java.io.File;


public class MainActivity extends BaseActivity implements UserManager.OnMyselfPrepareCallback, MenuItem.OnMenuItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewInject(R.id.main_drawer_layout)
    private DrawerLayout mMainDrawerLayout = null;

    private StatusListFragment mMainListFragment = null;
    private MySelfFragment mMySelfFragment = null;

    private ActionBarDrawerToggle mToggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        mMainListFragment = (StatusListFragment)getSupportFragmentManager().findFragmentById(R.id.main_status_list_fragment);
        mMySelfFragment = (MySelfFragment)getSupportFragmentManager().findFragmentById(R.id.main_user_fragment);
        mMySelfFragment.setOnMenuItemClickListener(this);

        mMainDrawerLayout.setDrawerListener(mToggle);

        UserManager.getInstance(this).registerOnMyselfPrepareCallback(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = mMainListFragment.getToolbar();
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainListFragment.smoothScrollTo(0);
            }
        });
        mToggle = new ActionBarDrawerToggle(this, mMainDrawerLayout, toolbar, R.string.app_name, R.string.hello_world);

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
        getSupportActionBar().setTitle(mySelf.name);
//        new HttpUtils ().download(mySelf.avatar_hd, CacheDirManager.getInstance(this).getTempCacheDir() + File.separator + mySelf.id + ".jpg",
//                new RequestCallBack<File>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        Bitmap bmp = BitmapFactory.decodeFile(responseInfo.result.getAbsolutePath());
//                        final float density = getResources().getDisplayMetrics().density;
//                        final int size = (int)(56 * density);
//                        bmp = Bitmap.createScaledBitmap(bmp, size, size, true);
//                        getSupportActionBar().setIcon(new BitmapDrawable(bmp));
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//
//                    }
//                });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_draft:
                startActivity(new Intent(this, DraftActivity.class));
                break;
        }
        mMainDrawerLayout.closeDrawers();
        return false;
    }
}
