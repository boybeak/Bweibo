/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.openapi.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 评论列表结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class CommentList implements Serializable {

    /** 微博列表 */
    public List<Comment> comments;
    public long previous_cursor;
    public long next_cursor;
    public int total_number;
    
    public static CommentList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        
        CommentList comments = new CommentList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            comments.previous_cursor = jsonObject.optLong("previous_cursor", 0);
            comments.next_cursor     = jsonObject.optLong("next_cursor", 0);
            comments.total_number    = jsonObject.optInt("total_number", 0);
            
            JSONArray jsonArray      = jsonObject.optJSONArray("comments");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                comments.comments = new ArrayList<Comment>(length);
                for (int ix = 0; ix < length; ix++) {
                    comments.comments.add(Comment.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> commentList) {
        this.comments = commentList;
    }

    public long getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(long previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
