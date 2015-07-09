package com.beak.bweibo.fragment.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beak.bweibo.R;
import com.beak.bweibo.activity.ToolbarActivity;
import com.beak.bweibo.manager.common.CacheDirManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sina.weibo.sdk.openapi.models.Thumbnail;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gaoyunfei on 15/7/8.
 */
public class ImagePreviewFragment extends Fragment implements
        PhotoViewAttacher.OnViewTapListener,
        PhotoViewAttacher.OnPhotoTapListener{

    private static final String TAG = ImagePreviewFragment.class.getSimpleName();

    @InjectView(R.id.preview_image)
    PhotoView mPreviewPv = null;
    @InjectView(R.id.preview_loading_progress_bar)
    ProgressBar mPreviewProgressBar;
    @InjectView(R.id.preview_loading_tip)
    TextView mPreviewTip;

    private File mBmpFile = null;

    private WeakReference<Bitmap> mBmpWeakRef = null;

    private Thumbnail mThumbnail = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_preview, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mPreviewPv.setOnViewTapListener(this);
        mPreviewPv.setOnPhotoTapListener(this);

        if (mBmpFile != null) {
            displayImage(mBmpFile);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mThumbnail != null) {
            setThumbnail(mThumbnail);
        }
    }

    public void setThumbnail (Thumbnail thumbnail) {
        mThumbnail = thumbnail;

        if (getActivity() == null) {
            return;
        }
        File targetFile = new File(CacheDirManager.getInstance(getActivity()).getTempCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        if (targetFile.exists()) {
            mBmpFile = targetFile;
            displayImage(mBmpFile);
            return;
        }
        if (mBmpFile != null) {
            displayImage(mBmpFile);
        } else {
            HttpUtils httpUtils = new HttpUtils(10 * 1000);
            httpUtils.download(
                    thumbnail.getBmiddle_pic(),
                    targetFile.getAbsolutePath(),
                    false, true,
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            mBmpFile = responseInfo.result;
                            displayImage(mBmpFile);
                            mPreviewProgressBar.setVisibility(View.GONE);
                            mPreviewTip.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            int progress = (int)(current * 100 / total);
                            mPreviewProgressBar.setProgress(progress);
                            mPreviewTip.setText(progress + "%");
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                        }
                    });
        }
    }

    private void displayImage (File file) {
        if (mBmpWeakRef == null || mBmpWeakRef.get() == null) {
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            mBmpWeakRef = new WeakReference<Bitmap>(bmp);
        }
        if (mPreviewPv != null) {
            mPreviewPv.setImageBitmap(mBmpWeakRef.get());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBmpFile != null) {
            mBmpFile.deleteOnExit();
        }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (getActivity() instanceof ToolbarActivity) {
            Toolbar toolbar = ((ToolbarActivity) getActivity()).getToolbar();
            if (toolbar.getVisibility() == View.VISIBLE) {
                toolbar.setVisibility(View.GONE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        getActivity().finish();
    }
}
