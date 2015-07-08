package com.beak.bweibo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beak.beakkit.debug.Debug;
import com.beak.bweibo.ActivityDispatcher;
import com.beak.bweibo.AutoDraftListener;
import com.beak.bweibo.DefaultRequestListener;
import com.beak.bweibo.Finals;
import com.beak.bweibo.R;
import com.beak.bweibo.Result;
import com.beak.bweibo.ResultCallback;
import com.beak.bweibo.manager.ApiManager;
import com.beak.bweibo.manager.CommentManager;
import com.beak.bweibo.manager.EmotionManager;
import com.beak.bweibo.manager.StatusManager;
import com.beak.bweibo.manager.common.ImageManager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Emotion;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends ToolbarActivity implements View.OnClickListener, ImageManager.OnImageGroupSelectedListener{

    private static final String TAG = PublishActivity.class.getSimpleName();

    public static final int
        PUBLISH_MODE_NORMAL = 0,
        PUBLISH_MODE_REPOST = 1,
        PUBLISH_MODE_COMMENT = 2;

    private int mMode = PUBLISH_MODE_NORMAL;

    @ViewInject(R.id.publish_input)
    private EditText mContentEt = null;
    @ViewInject(R.id.publish_camera)
    private ImageView mCameraIv = null;
    @ViewInject(R.id.publish_photo)
    private ImageView mGalleryIv = null;
    private List<ImageManager.ImageObject> mImageList = null;
    private Status mStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ViewUtils.inject(this);

        mCameraIv.setOnClickListener(this);
        mGalleryIv.setOnClickListener(this);

        mMode = getIntent().getIntExtra(Finals.KEY_PUBLISH_MODE, PUBLISH_MODE_NORMAL);
        mStatus = (Status)getIntent().getSerializableExtra(Finals.KEY_STATUS);
        EmotionManager.getInstance(this).getEmotion();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageManager.getInstance(this).clearTag(TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_camera:
                break;
            case R.id.publish_photo:
                ImageManager.getInstance(this).askFromGallery(TAG, 3, this);
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
                    CommentManager.getInstance(this)
                            .commentTo(mStatus, content, false, new CommentManager.CommentRequestListener(this) {
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
                    StatusManager.getInstance(this).repostStatus(mStatus, content, StatusManager.COMMENT_TYPE_NONE, new ResultCallback<Status>() {
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
