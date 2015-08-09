package com.beak.bweibo.activity.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.activity.ToolbarActivity;
import com.beak.bweibo.fragment.common.ImagePreviewFragment;
import com.lidroid.xutils.HttpUtils;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImagePreviewActivity extends ToolbarActivity {

    private static final String TAG = ImagePreviewActivity.class.getSimpleName();

    @InjectView(R.id.preview_view_pager)
    ViewPager mPreviewViewPager = null;

    private ArrayList<Thumbnail> mThumbnails = null;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ButterKnife.inject(this);


        mThumbnails = getIntent().getParcelableArrayListExtra(Finals.KEY_IMAGE_LIST);
        mIndex = getIntent().getIntExtra(Finals.KEY_INDEX, 0);
        mPreviewViewPager.setOffscreenPageLimit(1);
        if (mThumbnails != null) {
            mPreviewViewPager.setAdapter(new PreviewAdapter(getSupportFragmentManager()));
            mPreviewViewPager.setCurrentItem(mIndex);
        }
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    private class PreviewAdapter extends FragmentStatePagerAdapter {

        public PreviewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImagePreviewFragment fragment = new ImagePreviewFragment();
            fragment.setThumbnail(mThumbnails.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return mThumbnails.size();
        }
    }

}
