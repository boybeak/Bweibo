package com.sina.weibo.sdk.openapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gaoyunfei on 15/5/20.
 */
public class Thumbnail implements Serializable, Parcelable {

    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        if (bmiddle_pic == null) {
            bmiddle_pic = thumbnail_pic.replace("thumbnail", "bmiddle");
        }
        return bmiddle_pic;
    }

    public String getOriginal_pic() {
        if (original_pic == null) {
            original_pic = thumbnail_pic.replace("thumbnail", "large");
        }
        return original_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail_pic);
    }

    public static final Parcelable.Creator<Thumbnail> CREATOR
            = new Parcelable.Creator<Thumbnail>() {
        public Thumbnail createFromParcel(Parcel in) {
            return new Thumbnail(in);
        }

        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };

    public Thumbnail () {

    }

    private Thumbnail(Parcel in) {
        thumbnail_pic = in.readString();
    }
}
