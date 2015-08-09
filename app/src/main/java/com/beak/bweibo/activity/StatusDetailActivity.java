package com.beak.bweibo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.manager.CommentManager;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.openapi.models.FooterState;
import com.beak.bweibo.widget.adapter.CommentAdapter;
import com.beak.bweibo.widget.callback.TopTrackListener;
import com.beak.bweibo.widget.decoration.CommentDecoration;
import com.beak.bweibo.widget.callback.OnScrollBottomListener;
import com.beak.bweibo.widget.delegate.CommentDelegate;
import com.beak.bweibo.widget.delegate.StatusDelegate;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;
import java.util.List;

public class StatusDetailActivity extends ToolbarActivity {

    private static final String TAG = StatusDetailActivity.class.getSimpleName();

    private Status mStatus = null;

    private CommentAdapter mAdapter = null;

    private RecyclerView mRecyclerView = null;

    private LinearLayoutManager mLinearLayoutManager = null;

    private StatusDelegate mStatusDelegate = null;

    //private int mPage = 1;
    private long mNextCursor = 0;
    //private int mTotalCount = 0;

    private boolean isLoading = false;

    private OnScrollBottomListener mBottomListener = new OnScrollBottomListener() {
        @Override
        public void onScrollBottom(RecyclerView recyclerView, int newState) {
            if (!isLoading) {
                loadData(mNextCursor);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        mRecyclerView = (RecyclerView)findViewById(R.id.detail_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CommentAdapter(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CommentDecoration(this));
        mRecyclerView.addOnScrollListener(new TopTrackListener(getToolbar()));
        mRecyclerView.setOnScrollListener(mBottomListener);

        getToolbar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @Override
    public boolean isToolbarOverlay() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mStatus = (Status)getIntent().getSerializableExtra(Finals.KEY_STATUS);
        mAdapter.clearDataList();
        mStatusDelegate = new StatusDelegate(mStatus, false);
        mAdapter.addDataItem(mStatusDelegate);
        mAdapter.notifyDataSetChanged();
        StatusManager.getInstance(this).getStatusDetail(mStatus.id, new DefaultRequestListener<Status>(this, true, Status.class) {
            @Override
            public void onResult(Result<Status> result) {
                if (result.success) {
                    mAdapter.setDataItem(0, new StatusDelegate(result.data, false));
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        loadData(0);
    }

    @Override
    protected void onDestroy() {
        mRecyclerView.setAdapter(null);
        mAdapter.clearDataList();
        super.onDestroy();
    }

    private void loadData (final long nextCursor) {
        isLoading = true;
        mAdapter.setFooterState(FooterState.STATE_LOADING);
        mAdapter.notifyDataSetChanged();
        CommentManager.getInstance(this).getCommentList(mStatus.id, nextCursor, new DefaultRequestListener<CommentList>(this, CommentList.class) {
            @Override
            public void onResult(Result<CommentList> result) {
                isLoading = false;
                if (result.success) {
                    //mTotalCount = result.data.getTotal_number();
                    CommentList commentList = result.data;
                    mNextCursor = commentList.getNext_cursor();
                    Log.v(TAG, "loadData " + mNextCursor + " " + commentList.getPrevious_cursor());
                    List<CommentDelegate> delegates = new ArrayList<CommentDelegate>();
                    List<Comment> comments = commentList.comments;

                    final int length = comments.size();
                    for (int i = 0; i < length; i++) {
                        delegates.add(new CommentDelegate(comments.get(i)));
                    }

                    if (nextCursor == 0) {
                        mAdapter.clearDataList();
                        mAdapter.addDataItem(mStatusDelegate);
                    }
                    mAdapter.setFooterState(FooterState.STATE_SUCCESS);
                    mAdapter.addDataList(delegates);

                } else {
                    mAdapter.setFooterState(FooterState.STATE_FAILED);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
