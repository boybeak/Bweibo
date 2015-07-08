package com.beak.bweibo.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Emotion;

import java.util.List;
import java.util.Locale;

/**
 * Created by gaoyunfei on 15/6/12.
 */
public class EmotionManager extends BaseManager {

    private static final String TAG = EmotionManager.class.getSimpleName();

    public static final String
        TYPE_FACE = "face", TYPE_ANI = "ani", TYPE_CARTOON = "cartoon";
    public static final String
        LANGUAGE_CNNAME = "cnname", LANGUAGE_TWNAME = "twname";

    private static EmotionManager sManager = null;

    public synchronized static EmotionManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new EmotionManager(context.getApplicationContext());
        }
        return sManager;
    }

    public EmotionManager(Context context) {
        super(context);

    }

    public void getEmotion () {

        ApiManager.getInstance(getContext()).getStatusesApiInstance()
                .emotions("face", getLanguage(), new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        try {
                            List<Emotion> emotionList = new Gson().fromJson(s,
                                    new TypeToken<List<Emotion>>(){}.getType());
                            Log.v(TAG, "getEmotion emotionList.size=" + emotionList.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });
    }

    private String getLanguage () {
        Locale defaultLocal = Locale.getDefault();
        if (defaultLocal == Locale.TAIWAN ||
            defaultLocal == Locale.TRADITIONAL_CHINESE) {
            return LANGUAGE_TWNAME;
        }
        return LANGUAGE_CNNAME;
    }

}
