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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Comment implements Serializable {

    /** 评论创建时间 */
    public Date created_at;
    /** 评论的 ID */
    public long id;
    /** 评论的内容 */
    public String text;
    /** 评论的来源 */
    public String source;
    /** 评论作者的用户信息字段 */
    public User user;
    /** 评论的 MID */
    public String mid;
    /** 字符串型的评论 ID */
    public String idstr;
    /** 评论的微博信息字段 */
    public Status status;
    /** 评论来源评论，当本评论属于对另一评论的回复时返回此字段 */
    public Comment reply_comment;

    /**可能是赞的个数*/
    public int floor_num;
    
    public static Comment parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Comment comment = new Comment();
        //comment.created_at    = jsonObject.optString("created_at");
        comment.id            = jsonObject.optLong("id");
        comment.text          = jsonObject.optString("text");
        comment.source        = jsonObject.optString("source");
        comment.user          = User.parse(jsonObject.optJSONObject("user"));
        comment.mid           = jsonObject.optString("mid");
        comment.idstr         = jsonObject.optString("idstr");
        comment.status        = Status.parse(jsonObject.optJSONObject("status"));            
        comment.reply_comment = Comment.parse(jsonObject.optJSONObject("reply_comment"));
        
        return comment;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Comment getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment) {
        this.reply_comment = reply_comment;
    }

    public int getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(int floor_num) {
        this.floor_num = floor_num;
    }
}
