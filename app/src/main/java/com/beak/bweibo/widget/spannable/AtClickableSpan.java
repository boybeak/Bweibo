package com.beak.bweibo.widget.spannable;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beak.bweibo.ActivityDispatcher;

import java.util.regex.Pattern;

/**
 * Created by gaoyunfei on 15/6/11.
 */
public class AtClickableSpan extends URLSpan {


    public AtClickableSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        //"@磁爆线圈-X:"
        String url = getURL().replaceAll("[^\\w\\-\u4e00-\u9fa5]", "");
        Toast.makeText(widget.getContext(), url, Toast.LENGTH_SHORT).show();
        /*String url = getURL().replace("@", "");
        char lastChar = url.charAt(url.length() - 1);
        Pattern pattern = Pattern.compile("[^\\w\\-\u4e00-\u9fa5]");
        if (pattern.matcher(lastChar + "").matches()) {
            url = new StringBuilder(url).deleteCharAt(url.length() - 1).toString();
        }*/
        ActivityDispatcher.userActivity(widget.getContext(), url);
    }
}
