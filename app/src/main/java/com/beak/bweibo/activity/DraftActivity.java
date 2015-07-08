package com.beak.bweibo.activity;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beak.bweibo.R;
import com.beak.bweibo.manager.common.DraftManager;
import com.beak.bweibo.openapi.models.Draft;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class DraftActivity extends ToolbarActivity {

    private static final String TAG = DraftActivity.class.getSimpleName();

    @ViewInject(R.id.draft_debug_btn)
    private Button mAddBtn = null;
    @ViewInject(R.id.draft_debug_count)
    private TextView mCountTv = null;

    private Handler mHandler = new Handler() {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);

        ViewUtils.inject(this);

    }

    @Override
    public boolean isHomeAsUpEnable() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        List<Draft> list = DraftManager.getInstance(this).getDraftList();

        if (list != null) {
            mCountTv.setText(list.size() + "");
        }

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Draft draft = new Draft();
                draft.setContent("ABC");
                draft.setErrorMsg("def");*/
                //boolean success = DraftManager.getInstance(DraftActivity.this).save(draft);
                //Toast.makeText(DraftActivity.this, "success=" + success, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class DraftContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DraftContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.v(TAG, "onChange one");
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.v(TAG, "onChange two");
        }
    }
}
