package com.sina.weibo.sdk.openapi.models;

import java.io.Serializable;

/**
 * Created by gaoyunfei on 15/6/12.
 */
public class Emotion implements Serializable {
    public String phrase;
    public String type;
    public String url;
    public boolean hot;
    public boolean common;
    public String category;
    public String icon;
    public String value;
    public String picid;
}
