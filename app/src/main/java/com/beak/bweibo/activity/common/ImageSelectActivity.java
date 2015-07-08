package com.beak.bweibo.activity.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.beak.bweibo.R;
import com.beak.bweibo.activity.ToolbarActivity;
import com.beak.bweibo.fragment.common.GalleryFragment;
import com.beak.bweibo.manager.common.ImageManager;

public class ImageSelectActivity extends ToolbarActivity implements GalleryFragment.OnImageItemClickedListener{

    private String mSelectTag = null;

    private GalleryFragment mGalleryFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        mSelectTag = getIntent().getStringExtra(ImageManager.KEY_TAG);

        mGalleryFragment = (GalleryFragment)getSupportFragmentManager().findFragmentById(R.id.image_slect_fragment);
        mGalleryFragment.setOnImageItemClickedListener(this);
        mGalleryFragment.setTag(mSelectTag);
    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @Override
    public boolean onPreClicked(ImageManager.ImageObject object, boolean isSelected) {
        if (isSelected) {
            ImageManager.getInstance(this).removeImageFrom(mSelectTag, object);
            return true;
        }
        boolean accept = ImageManager.getInstance(this).addImageTo(mSelectTag, object);
        if (!accept) {
            ImageManager.ImageGroup group = ImageManager.getInstance(this).getImageGroup(mSelectTag);
            if (group != null) {
                Toast.makeText(this, getString(R.string.toast_max_image_count_reached, group.getMaxCount()), Toast.LENGTH_SHORT).show();
            }
        }
        return accept;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_select, menu);
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
            ImageManager.getInstance(this).flushImageGroup(mSelectTag);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
