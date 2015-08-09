package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beak.bweibo.R;
import com.beak.bweibo.widget.delegate.MenuItemDelegate;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gaoyunfei on 15/7/28.
 */
public class MenuItemHolder extends AbsViewHolder<MenuItemDelegate> {

    @InjectView(R.id.menu_item_icon)
    public ImageView menuIconIv;
    @InjectView(R.id.menu_item_title)
    public TextView menuTitleTv;
    @InjectView(R.id.menu_item_count)
    public TextView menuCountTv;

    public MenuItemHolder(View itemView) {
        super(itemView);

        ButterKnife.inject(this, itemView);
    }

    @Override
    public void bindData(Context context, MenuItemDelegate menuItemDelegate) {
        MenuItem menuItem = menuItemDelegate.getSource();
        menuIconIv.setImageDrawable(menuItem.getIcon());
        menuTitleTv.setText(menuItem.getTitle());
        menuCountTv.setText(menuItemDelegate.getCount() + "");
    }
}
