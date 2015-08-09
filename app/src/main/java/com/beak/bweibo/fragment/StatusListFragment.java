package com.beak.bweibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beak.beakkit.utils.UiUtils;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.activity.PublishActivity;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.openapi.models.FooterState;
import com.beak.bweibo.widget.adapter.StatusAdapter;
import com.beak.bweibo.widget.decoration.StatusDecoration;
import com.beak.bweibo.widget.callback.BottomTrackListener;
import com.beak.bweibo.widget.callback.TopTrackListener;
import com.beak.bweibo.widget.callback.OnScrollBottomListener;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by gaoyunfei on 15/5/16.
 */
public class StatusListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener, StatusManager.StatusCallback {

    private static final String TAG = StatusListFragment.class.getSimpleName();

    @InjectView(R.id.status_list_swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout = null;

    @InjectView(R.id.main_tool_bar)
    Toolbar mMainToolbar = null;

    @InjectView(R.id.status_list_recycler_view)
    RecyclerView mRecyclerView = null;

    @InjectView(R.id.status_list_publish_btn)
    FloatingActionButton mActionBtn = null;

    private LinearLayoutManager mLinearManager = null;

    private StatusAdapter mAdapter = null;

    //private int mIndex = 1;
    private long mLastStatusId = 0;
    private boolean isLoading = false;

    private OnScrollBottomListener mBottomListener = new OnScrollBottomListener() {
        @Override
        public void onScrollBottom(RecyclerView recyclerView, int newState) {
            if (!isLoading) {
                loadData(/*mIndex + 1*/mLastStatusId);
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new StatusAdapter(getActivity());
        StatusManager.getInstance(getActivity()).registerStatusCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        final int actionBarHeight = UiUtils.getActionBarHeight(getActivity());
        mRefreshLayout.setProgressViewOffset(false, actionBarHeight, actionBarHeight * 2);

        mLinearManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLinearManager);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mBottomListener);
        mRecyclerView.addOnScrollListener(new TopTrackListener(mMainToolbar));
        mRecyclerView.addOnScrollListener(new BottomTrackListener(mActionBtn));
        mRecyclerView.addItemDecoration(new StatusDecoration(getActivity()));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRefreshLayout.setOnRefreshListener(this);
        mActionBtn.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                loadData(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StatusManager.getInstance(getActivity()).unregisterStatusCallback(this);
    }

    @Override
    public void onRefresh() {
        loadData(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_list_publish_btn:
                startActivity(new Intent(getActivity(), PublishActivity.class));
                break;
        }
    }

    public void loadData (final /*int index*/long maxId) {
        isLoading = true;
        mAdapter.setFooterState(mAdapter.getItemCount() <= 1 ? FooterState.STATE_NONE : FooterState.STATE_LOADING);
        mAdapter.notifyDataSetChanged();
        StatusManager.getInstance(getActivity()).homeTimeline(maxId, new DefaultRequestListener<StatusList>(getActivity(), false, StatusList.class) {
            @Override
            public void onResult(Result<StatusList> result) {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                List<StatusDelegate> delegates = new ArrayList<StatusDelegate>();
                if (result.success) {
                    //mIndex = index;
                    StatusList statusList = result.data;

                    mLastStatusId = statusList.getNext_cursor();


                    List<Status> statuses = statusList.statuses;
                    final int length = statuses.size();
                    for (int i = 0; i < length; i++) {
                        Status s = statuses.get(i);
                        delegates.add(new StatusDelegate(s));
                    }
                    if (maxId == 0) {
                        mAdapter.clearDataList();
                    }
                    mAdapter.setFooterState(FooterState.STATE_SUCCESS);
                } else {
                    mAdapter.setFooterState(FooterState.STATE_FAILED);

                }
                mAdapter.addDataList(delegates);
                isLoading = false;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void smoothScrollTo (int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void onStatusCreate(String callbackId, Status status) {

    }

    @Override
    public void onStatusPublished(String callbackId, Result<Status> result) {

    }

    @Override
    public void onStatusRemoved(String callbackId, Status status) {

    }

    public Toolbar getToolbar () {
        return mMainToolbar;
    }
}
