package com.beak.bweibo.manager.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beak.bweibo.activity.common.ImageSelectActivity;
import com.beak.bweibo.manager.BaseManager;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaoyunfei on 15/5/20.
 */
public final class ImageManager extends BaseManager {

    private static final String TAG = ImageManager.class.getSimpleName();

    public static final String KEY_TAG = "ImageManager.key_tag";

    private static ImageManager sManager = null;

    public synchronized static ImageManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new ImageManager(context.getApplicationContext());
        }
        return sManager;
    }

    private Map<String, ImageGroup> mTagMap = new HashMap<String, ImageGroup>();

    private OnImageGroupSelectedListener mImageGroupSelectedListener = null;

    private ImageManager(Context context) {
        super(context);

    }

    public void askFromGallery (String tag, int max, OnImageGroupSelectedListener listener) {
        mImageGroupSelectedListener = listener;
        boolean hasOne = mTagMap.containsKey(tag);
        ImageGroup group = null;
        if (!hasOne) {
            group = new ImageGroup(max);
            mTagMap.put(tag, group);
        }
        Intent it = new Intent(getContext(), ImageSelectActivity.class);
        it.putExtra(KEY_TAG, tag);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(it);
    }

    public ImageGroup getImageGroup (String tag) {
        return mTagMap.get(tag);
    }

    public boolean addImageTo(String tag, ImageObject imageObject) {
        ImageGroup group = mTagMap.get(tag);
        return group.addImageObject(imageObject);
    }

    public void removeImageFrom(String tag, ImageObject imageObject) {
        if (mTagMap.containsKey(tag)) {
            mTagMap.get(tag).removeImageObject(imageObject);
        }
    }

    public boolean containsImageAt (String tag, ImageObject imageObject) {
        if (mTagMap.containsKey(tag)) {
            return mTagMap.get(tag).containsImageObject(imageObject);
        }
        return false;
    }

    public void flushImageGroup (String tag) {
        ImageGroup group = mTagMap.get(tag);
        if (mImageGroupSelectedListener != null) {
            boolean clear = mImageGroupSelectedListener.onSelected(group);
            if (clear) {
                mTagMap.remove(tag);
            }
            mImageGroupSelectedListener = null;
        }
    }

    public void clearTag (String tag) {
        mTagMap.remove(tag);
    }

    public static class ImageGroup {
        private int mMaxCount = 0;
        private List<ImageObject> mImageList = null;
        public ImageGroup (int maxCount) {
            mMaxCount = maxCount;
            mImageList = new ArrayList<ImageObject>();
        }

        public boolean addImageObject (ImageObject imageObject) {
            Log.v(TAG, "addImageObject size " + mImageList.size() + " max=" + mMaxCount);
            if (mImageList.size() < mMaxCount) {
                mImageList.add(imageObject);
                return true;
            }
            return false;
        }

        public void removeImageObject (ImageObject imageObject) {
            mImageList.remove(imageObject);
        }

        public int getMaxCount () {
            return mMaxCount;
        }

        public List<ImageObject> getImageList () {
            return mImageList;
        }

        public boolean containsImageObject (ImageObject object) {
            return mImageList.contains(object);
        }
    }

    public static class ImageObject {
        //public int id;

        @SerializedName("_display_name")
        public String displayName;
        //public long latitude, longitude;

        @SerializedName("_data")
        public String data;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof ImageObject) {
                return ((ImageObject) o).data.equals(data);
            }
            return false;
        }
    }

    public static interface OnImageGroupSelectedListener {
        /**
         * @param group the group contains selected images
         * @return clear the selected images for this tag after user if true
         */
        public boolean onSelected (ImageGroup group);
    }

}
