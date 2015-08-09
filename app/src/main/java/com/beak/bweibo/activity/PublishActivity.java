package com.beak.bweibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.ResultCallback;
import com.beak.bweibo.fragment.SummaryFragment;
import com.beak.bweibo.manager.CommentManager;
import com.beak.bweibo.manager.EmotionManager;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.manager.common.ImageManager;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PublishActivity extends ToolbarActivity implements ImageManager.OnImageGroupSelectedListener{

    private static final String TAG = PublishActivity.class.getSimpleName();

    public static final int
        PUBLISH_MODE_NORMAL = 0,
        PUBLISH_MODE_REPOST = 1,
        PUBLISH_MODE_COMMENT = 2,
        PUBLISH_MODE_REPLY_TO_COMMENT = 3,
        PUBLISH_MODE_REPOST_A_COMMENT = 4;

    private int mMode = PUBLISH_MODE_NORMAL;

    @InjectView(R.id.publish_input) EditText mContentEt = null;
    @InjectView(R.id.publish_emotion) ImageView mEmotionIv = null;
    @InjectView(R.id.publish_camera) ImageView mCameraIv = null;
    @InjectView(R.id.publish_photo) ImageView mGalleryIv = null;
    @InjectView(R.id.publish_at) ImageView mAtIv = null;
    @InjectView(R.id.publish_topic) ImageView mTopicIv = null;
    @InjectView(R.id.publish_repost_extra) LinearLayout mRepostExtraLayout = null;
    @InjectView(R.id.publish_repost_with_comment_this) CheckBox mRepostWithCommentThisCb = null;
    @InjectView(R.id.publish_repost_with_comment_org) CheckBox mRepostWithCommentOrgCb = null;
    @InjectView(R.id.publish_comment_extra) LinearLayout mCommentExtraLayout = null;
    @InjectView(R.id.publish_comment_with_repost) CheckBox mCommentWithRepostCb = null;
    @InjectView(R.id.publish_comment_also_to_org) CheckBox mCommentAlsoToOrgCb = null;
    @InjectView(R.id.publish_summary_container) FrameLayout mSummaryContainer = null;

    private SummaryFragment mSummaryFragment = null;


    private List<ImageManager.ImageObject> mImageList = null;
    private Status mStatus = null;
    private Comment mComment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        mSummaryFragment = (SummaryFragment)getSupportFragmentManager().findFragmentById(R.id.publish_summary);

        ButterKnife.inject(this);

//        if (mStatus != null && mStatus.hasRetweetOne()) {
//            mRepostWithCommentOrgCb.setVisibility(View.VISIBLE);
//            Status retweetOne = mStatus.getRetweeted_status();
//            User user = retweetOne.user;
//            if (user != null) {
//                mRepostWithCommentOrgCb.setText(getString(R.string.publish_repost_with_comment_org, user.name));
//            }
//
//        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMode = getIntent().getIntExtra(Finals.KEY_PUBLISH_MODE, PUBLISH_MODE_NORMAL);
        EmotionManager.getInstance(this).getEmotion();
        Intent it = getIntent();

        User user = null;

        switch (mMode) {
            case PUBLISH_MODE_NORMAL:
                mSummaryContainer.setVisibility(View.GONE);
                //mStatus = (Status)it.getSerializableExtra(Finals.KEY_STATUS);
                break;
            case PUBLISH_MODE_REPOST:
                mStatus = (Status)it.getSerializableExtra(Finals.KEY_STATUS);
                user = mStatus.user;
                mSummaryFragment.setSummaryProvider(mStatus);
                mRepostExtraLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(R.string.title_activity_repost);
                if (mStatus != null && mStatus.hasRetweetOne()) {
                    mRepostWithCommentOrgCb.setVisibility(View.VISIBLE);
                    Status retweetOne = mStatus.retweeted_status;
                    User retweetUser = retweetOne.user;
                    if (user != null) {
                        mRepostWithCommentOrgCb.setText(getString(R.string.publish_repost_with_comment_org, retweetUser.name));
                    }

                }
                mCameraIv.setVisibility(View.GONE);
                mGalleryIv.setVisibility(View.GONE);
                break;
            case PUBLISH_MODE_COMMENT:
                mStatus = (Status)it.getSerializableExtra(Finals.KEY_STATUS);
                user = mStatus.user;
                if (user != null) {
                    mContentEt.setHint(getString(R.string.hint_publish_input_et_comment, user.name));
                }

                mSummaryFragment.setSummaryProvider(mStatus);
                mCommentExtraLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(R.string.title_activity_comment);
                if (mStatus != null && mStatus.hasRetweetOne()) {
                    mCommentAlsoToOrgCb.setVisibility(View.VISIBLE);
                    Status retweetOne = mStatus.retweeted_status;
                    User retweetUser = retweetOne.user;
                    if (user != null) {
                        mCommentAlsoToOrgCb.setText(getString(R.string.publish_comment_also_to_org, retweetUser.name));
                    }

                }
                mCameraIv.setVisibility(View.GONE);
                mGalleryIv.setVisibility(View.GONE);
                break;
            case PUBLISH_MODE_REPLY_TO_COMMENT:
                mComment = (Comment)it.getSerializableExtra(Finals.KEY_COMMENT);
                mSummaryFragment.setSummaryProvider(mComment);
                user = mComment.user;
                if (user != null) {
                    mContentEt.setHint(getString(R.string.hint_publish_input_et_comment, user.name));
                }
                getSupportActionBar().setTitle(R.string.title_activity_comment);
                //mCommentExtraLayout.setVisibility(View.VISIBLE);
                mCameraIv.setVisibility(View.GONE);
                mGalleryIv.setVisibility(View.GONE);
                break;
            case PUBLISH_MODE_REPOST_A_COMMENT:
                mCameraIv.setVisibility(View.GONE);
                mGalleryIv.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageManager.getInstance(this).clearTag(TAG);
    }

    @OnClick({R.id.publish_emotion, R.id.publish_camera, R.id.publish_photo, R.id.publish_at, R.id.publish_topic})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.publish_camera:
                break;
            case R.id.publish_photo:
                ImageManager.getInstance(this).askFromGallery(TAG, 3, this);
                break;
            case R.id.publish_at:
                break;
        }
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            String content = mContentEt.getText().toString();

            switch (mMode) {
                case PUBLISH_MODE_COMMENT:
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(this, R.string.toast_publish_comment_empty_content, Toast.LENGTH_SHORT).show();
                        return super.onOptionsItemSelected(item);
                    }
                    if (mCommentWithRepostCb.isChecked()) {
                        StatusManager.getInstance(this).repostStatus(mStatus, content, 0, new ResultCallback<Status>() {
                            @Override
                            public void onResult(Result<Status> result) {
                                /*Toast.makeText(PublishActivity.this,
                                        result.success ? R.string.toast_publish_repost_success : R.string.toast_publish_repost_failed,
                                        Toast.LENGTH_SHORT).show();*/
                            }
                        });
                    }
                    CommentManager.getInstance(this)
                            .commentTo(mStatus, content, mCommentAlsoToOrgCb.isChecked(), new CommentManager.CommentRequestListener(this) {
                                @Override
                                public void onResult(Result<Comment> result) {
                                    super.onResult(result);
                                    Toast.makeText(PublishActivity.this,
                                            result.success ? R.string.toast_publish_comment_success : R.string.toast_publish_comment_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case PUBLISH_MODE_REPOST:
                    if (TextUtils.isEmpty(content)) {
                        content = getString(R.string.publish_repost_empty_content);
                    }
                    int commentType = 0;
                    if (mRepostWithCommentThisCb.isChecked()) {
                        commentType += 1;
                    }
                    if (mRepostWithCommentOrgCb.isChecked()) {
                        commentType += 2;
                    }
                    StatusManager.getInstance(this).repostStatus(mStatus, content, commentType, new ResultCallback<Status>() {
                        @Override
                        public void onResult(Result<Status> result) {
                            Toast.makeText(PublishActivity.this,
                                    result.success ? R.string.toast_publish_repost_success : R.string.toast_publish_repost_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case PUBLISH_MODE_NORMAL:
                    if (mImageList == null || mImageList.isEmpty()) {
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(this, R.string.toast_publish_empty_content, Toast.LENGTH_SHORT).show();
                            return super.onOptionsItemSelected(item);
                        }
                        StatusManager.getInstance(this).publishStatus(content, new ResultCallback<Status>() {
                            @Override
                            public void onResult(Result<Status> result) {
                                Toast.makeText(PublishActivity.this,
                                        result.success ? R.string.toast_publish_success : R.string.toast_publish_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (TextUtils.isEmpty(content)) {
                            content = getString(R.string.publish_empty_content);
                        }

                        List<String> paths = new ArrayList<String>();
                        for (ImageManager.ImageObject object : mImageList) {
                            paths.add(object.data);
                        }
                        StatusManager.getInstance(this).uploadStatus(content, paths, new ResultCallback() {
                            @Override
                            public void onResult(Result result) {
                                Toast.makeText(PublishActivity.this,
                                        result.success ? R.string.toast_publish_success : R.string.toast_publish_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case PUBLISH_MODE_REPLY_TO_COMMENT:
                    if (mComment != null) {
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(this, R.string.toast_publish_empty_content, Toast.LENGTH_SHORT).show();
                            return super.onOptionsItemSelected(item);
                        }
                        CommentManager.getInstance(this).replyTo(mComment, content, true, new CommentManager.CommentRequestListener(this) {
                            @Override
                            public void onResult(Result<Comment> result) {
                                super.onResult(result);
                                Toast.makeText(PublishActivity.this,
                                        result.success ? R.string.toast_publish_comment_success : R.string.toast_publish_comment_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }

            finish();
            /*ApiManager.getInstance(this).getStatusesApiInstance().update(content, "0", "0", new RequestListener() {
                @Override
                public void onComplete(String s) {

                    finish();
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Toast.makeText(PublishActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });*/
            return true;
        }/* else if (id == android.R.id.home) {
            onBackPressed();
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSelected(ImageManager.ImageGroup group) {
        mImageList = group.getImageList();
        return false;
    }
}
