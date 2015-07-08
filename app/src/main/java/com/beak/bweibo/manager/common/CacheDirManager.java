package com.beak.bweibo.manager.common;

import android.content.Context;
import android.os.Environment;

import com.beak.bweibo.manager.BaseManager;

import java.io.File;

/**
 * Created by gaoyunfei on 15/7/2.
 */
public class CacheDirManager extends BaseManager {

    public static CacheDirManager sManager = null;

    public synchronized static CacheDirManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new CacheDirManager(context.getApplicationContext());
        }
        return sManager;
    }

    private File mCacheRootFile = null;

    public CacheDirManager(Context context) {
        super(context);
        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            mCacheRootFile = context.getExternalCacheDir();
        } else {
            mCacheRootFile = context.getCacheDir();
        }
    }

    public File makeDirUnderCache (String dirName) {
        File file = new File(mCacheRootFile, dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getExceptionCacheDir () {
        return makeDirUnderCache("exception");
    }

    public File getTempCacheDir () {
        return makeDirUnderCache("temp");
    }

}
