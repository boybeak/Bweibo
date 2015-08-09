package com.beak.bweibo.widget.delegate;

import android.view.MenuItem;

/**
 * Created by gaoyunfei on 15/7/28.
 */
public class MenuItemDelegate extends BaseDelegate<MenuItem> {

    private int mCount = 0;

    public MenuItemDelegate(MenuItem menuItem) {
        super(menuItem);
    }

    @Override
    public DelegateType getDelegateType() {
        return DelegateType.MENU_ITEM;
    }

    public void setCount (int count) {
        mCount = count;
    }

    public int getCount () {
        return mCount;
    }
}
