package com.beak.bweibo.widget.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

import com.beak.bweibo.ActivityDispatcher;

/**
 * Created by gaoyunfei on 15/6/11.
 */
public class TopicClickableSpan extends URLSpan {

    public TopicClickableSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        ActivityDispatcher.topicActivity(widget.getContext(), getURL());
    }
}
