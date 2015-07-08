package com.beak.bweibo.utils;

import android.text.SpannableString;
import android.text.Spanned;

import com.beak.bweibo.widget.spannable.AtClickableSpan;
import com.beak.bweibo.widget.spannable.TopicClickableSpan;
import com.beak.bweibo.widget.spannable.UrlClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaoyunfei on 15/6/10.
 */
public class PatternUtils {
    static Pattern
            URL = Pattern.compile("http://t.cn/\\w+"),
            AT = Pattern.compile("@[\\w\\-\u4e00-\u9fa5]{1,30}\\W?"),
            TOPIC = Pattern.compile("#[^#]+#"), //"#[\\w\u4e00-\u9fa5]+#"
            EMOTION = Pattern.compile("\\[[\\w\u4e00-\u9fa5]+\\]");

    public static CharSequence translate (CharSequence cs) {

        return manageTopic(manageUrl(manageAt(cs)));
    }

    public static CharSequence manageEmotion (CharSequence cs) {
        return cs;
    }

    public static CharSequence manageAt (CharSequence cs) {
        Matcher matcher = AT.matcher(cs);
        SpannableString spannable = new SpannableString(cs);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();
            Pattern pattern = Pattern.compile("@[\\w一-龥]{1,30}\\W");
            int delta = 0;
            String at = matcher.group();
            Matcher innerMatcher = pattern.matcher(group);
            if (innerMatcher.find()) {
                at = innerMatcher.group();
                delta = 1;
            }
            AtClickableSpan atSpan = new AtClickableSpan(at);
            spannable.setSpan(atSpan, start, end - delta, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static CharSequence manageUrl (CharSequence cs) {
        Matcher matcher = URL.matcher(cs);
        SpannableString spannable = new SpannableString(cs);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String url = matcher.group();
            UrlClickableSpan span = new UrlClickableSpan(url);
            spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static CharSequence manageTopic (CharSequence cs) {
        Matcher matcher = TOPIC.matcher(cs);
        SpannableString spannable = new SpannableString(cs);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String topic = matcher.group();
            TopicClickableSpan span = new TopicClickableSpan(topic);
            spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }
}
