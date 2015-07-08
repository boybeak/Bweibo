package com.beak.bweibo.widget.delegate;

import com.sina.weibo.sdk.openapi.models.Ad;

/**
 * Created by gaoyunfei on 15/5/23.
 */
public class AdDelegate extends BaseDelegate<Ad> {

    public AdDelegate(Ad ad) {
        super(ad);
    }

    @Override
    public DelegateType getDelegateType() {
        return null;
    }
}
