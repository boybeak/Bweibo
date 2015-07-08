package com.beak.bweibo.widget.delegate;

import com.beak.bweibo.utils.PatternUtils;
import com.sina.weibo.sdk.openapi.models.Comment;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class CommentDelegate extends BaseDelegate<Comment> {

    private CharSequence mSpannable = null;

    public CommentDelegate(Comment comment) {
        super(comment);
        mSpannable = PatternUtils.translate(comment.getText());
    }

    @Override
    public DelegateType getDelegateType() {
        return DelegateType.COMMENT_;
    }

    public CharSequence getSpnnable () {
        return mSpannable;
    }
}
