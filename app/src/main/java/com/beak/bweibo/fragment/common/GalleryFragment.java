package com.beak.bweibo.fragment.common;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.beak.beakkit.debug.Debug;
import com.beak.beakkit.manager.media.DisplayManager;
import com.beak.bweibo.R;
import com.beak.bweibo.manager.common.ImageManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 15/5/24.
 */
public class GalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = GalleryFragment.class.getSimpleName();

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE
    };

    private RecyclerView mGalleryRv = null;
    private GridLayoutManager mLayoutManager = null;
    private List<ImageObjectDelegate> mDataList = new ArrayList<ImageObjectDelegate>();
    private ImageAdapter mAdapter = null;

    private OnImageItemClickedListener mImageClickListener = null;

    private String mTag = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGalleryRv = (RecyclerView)view.findViewById(R.id.gallery_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 4);
        mGalleryRv.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter(getActivity(), mDataList);
        mGalleryRv.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                getActivity(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                null
        );
        return loader;
    }

    public void setTag (String tag) {
        mTag = tag;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<ImageObjectDelegate> delegates = new ArrayList<ImageObjectDelegate>();
        for (data.moveToFirst(); data.moveToNext();) {
            JsonObject jsonObject = new JsonObject();
            for (int i = 0; i < STORE_IMAGES.length; i++) {
                jsonObject.addProperty(data.getColumnName(i), data.getString(i));
            }
            ImageManager.ImageObject object = new Gson().fromJson(jsonObject, ImageManager.ImageObject.class);
            ImageObjectDelegate delegate = new ImageObjectDelegate(object);
            delegate.isChecked = ImageManager.getInstance(getActivity()).containsImageAt(mTag, object);
            delegates.add(delegate);
        }
        mDataList.clear();
        mDataList.addAll(delegates);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setOnImageItemClickedListener (OnImageItemClickedListener listener) {
        mImageClickListener = listener;
    }

    private class ImageObjectDelegate {

        private boolean isChecked = false;
        private ImageManager.ImageObject mImageObject = null;

        public ImageObjectDelegate (ImageManager.ImageObject object) {
            mImageObject = object;
        }
    }
    private class ImageHolder extends RecyclerView.ViewHolder {

        public ImageView image = null;
        public CheckBox checkBox = null;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.item_img);
            checkBox = (CheckBox)itemView.findViewById(R.id.item_check_box);
        }
    }
    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

        private Context mContext = null;
        private List<ImageObjectDelegate> mDataList = null;

        public ImageAdapter (Context context, List<ImageObjectDelegate> delegateList) {
            mContext = context;
            mDataList = delegateList;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_image_item, null);
            ImageHolder holder = new ImageHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            final ImageObjectDelegate delegate = mDataList.get(position);
            final ImageManager.ImageObject image = delegate.mImageObject;
            holder.checkBox.setChecked(delegate.isChecked);
            DisplayManager.getInstance(mContext)
                    .getImageLoaderInstance()
                    .displayImage(ImageDownloader.Scheme.FILE.wrap(image.data), holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageClickListener != null && mTag != null) {
                        if (mImageClickListener.onPreClicked(image, delegate.isChecked)) {
                            delegate.isChecked = !delegate.isChecked;
                            notifyDataSetChanged();
                        }

                    } else {
                        throw new NullPointerException("you must set a OnImageItemClickedListener and a tag for GalleryFragment");
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    public static interface OnImageItemClickedListener {
        public boolean onPreClicked (ImageManager.ImageObject object, boolean isSelected);
    }
}
