package com.beak.bweibo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.manager.UserManager;
import com.beak.bweibo.widget.adapter.StatusAdapter;
import com.beak.bweibo.widget.decoration.StatusDecoration;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gaoyunfei on 15/5/28.
 */
public class UserFragment extends Fragment {

    private User mUser = null;

    @InjectView(R.id.user_profile)
    ImageView mProfileIv = null;
    @InjectView(R.id.user_status_list)
    RecyclerView mUserStatusListRv = null;

    private StatusAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mAdapter = new StatusAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mUserStatusListRv.setLayoutManager(linearLayoutManager);
        mUserStatusListRv.setAdapter(mAdapter);
        mUserStatusListRv.addItemDecoration(new StatusDecoration(getActivity()));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadUser (String screenName) {
        UserManager.getInstance(getActivity()).getUser(screenName, new DefaultRequestListener<User>(getActivity(), User.class) {
            @Override
            public void onResult(Result<User> result) {
                if (isDetached()) {
                    return;
                }
                if (result.success) {
                    fillUser(getContext(), result.data);
                    loadUserStatusList (result.data.getId());
                }
            }
        });
    }

    public void loadUser (long uid) {
        UserManager.getInstance(getActivity()).getUser(uid, new DefaultRequestListener<User>(getActivity(), User.class) {
            @Override
            public void onResult(Result<User> result) {
                if (isDetached()) {
                    return;
                }
                if (result.success) {
                    fillUser(getContext(), result.data);
                }
            }
        });
        loadUserStatusList(uid);
    }

    public void loadUser (User user) {
        fillUser(getActivity(), user);
        loadUser(user.getId());
    }

    private void fillUser (Context context, User user) {
        mUser = user;
        DisplayManager.getInstance(context)
                .getImageLoaderInstance()
                .displayImage(mUser.avatar_hd, mProfileIv, DisplayManager.getDefaultDisplayImageOptions());
    }

    private void loadUserStatusList (long uid) {
        StatusManager.getInstance(getActivity()).userTimeline(uid, 0, new DefaultRequestListener<StatusList>(getActivity(), StatusList.class) {
            @Override
            public void onResult(Result<StatusList> result) {
                if (result.success) {
                    StatusList statusList = result.data;
                    List<Status> list = statusList.getStatuses();
                    final int length = list.size();
                    List<StatusDelegate> delegates = new ArrayList<StatusDelegate>();
                    for (int i = 0; i < length; i++) {
                        delegates.add(new StatusDelegate(list.get(i)));
                    }
                    mAdapter.addDataList(delegates);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
