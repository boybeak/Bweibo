package com.beak.bweibo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.DefaultImageLoadingListener;
import com.beak.bweibo.R;
import com.beak.bweibo.activity.DraftActivity;
import com.beak.bweibo.manager.UserManager;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sina.weibo.sdk.openapi.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gaoyunfei on 15/6/15.
 */
public class MySelfFragment extends Fragment implements UserManager.OnMyselfPrepareCallback, NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MySelfFragment.class.getSimpleName();

    @InjectView(R.id.header_profile) ImageView mHeaderProfileIv = null;
    @InjectView(R.id.header_cover) ImageView mHeaderCoverIv = null;
    @InjectView(R.id.header_name) TextView mHeaderNameTv = null;
    @InjectView(R.id.header_location) TextView mHeaderLocationTv = null;
    @InjectView(R.id.header_desc) TextView mHaaderDescTv = null;

    @InjectView(R.id.myself_navigationview) NavigationView mNavigationView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserManager.getInstance(getActivity()).registerOnMyselfPrepareCallback(this);
        UserManager.getInstance(getActivity()).refreshMySelf();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myself, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mNavigationView.setNavigationItemSelectedListener(this);
        User mySelf = UserManager.getInstance(getActivity()).getMySelf();
        if (mySelf != null) {
            fillUser(mySelf);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserManager.getInstance(getActivity()).unregisterOnMyselfPreparedCallback(this);
    }

    @Override
    public void onPrepared(User mySelf) {
        fillUser(mySelf);
    }

    private void fillUser (final User mySelf) {
        DisplayManager.getInstance(getActivity())
                .displayRoundImage(
                        mySelf.avatar_hd,
                        getResources().getDimensionPixelSize(R.dimen.profile_size_in_list),
                        mHeaderProfileIv,
                        DefaultImageLoadingListener.getInstance());
        DisplayManager.getInstance(getActivity())
                .getImageLoaderInstance()
                .displayImage(
                        mySelf.cover_image_phone,
                        mHeaderCoverIv,
                        DisplayManager.getDefaultDisplayImageOptions(),
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch swatch = palette.getDarkMutedSwatch();
                                        if (swatch == null) {
                                            swatch = palette.getLightMutedSwatch();
                                        }
                                        if (swatch != null) {
                                            mHeaderNameTv.setTextColor(swatch.getTitleTextColor());
                                            mHeaderLocationTv.setTextColor(swatch.getTitleTextColor());
                                            mHaaderDescTv.setTextColor(swatch.getBodyTextColor());
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        }
                );
        mHeaderNameTv.setText(mySelf.name);
        mHeaderLocationTv.setText(mySelf.location);
        mHaaderDescTv.setText(mySelf.description);

        mHeaderProfileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDispatcher.userActivity(getActivity(), mySelf);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_draft:
                startActivity(new Intent(getActivity(), DraftActivity.class));
                break;
        }
        return false;
    }
}
