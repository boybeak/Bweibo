package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.beak.bweibo.widget.delegate.AbsDelegate;
import com.beak.bweibo.widget.delegate.DelegateType;
import com.beak.bweibo.widget.delegate.MenuItemDelegate;
import com.beak.bweibo.widget.holder.AbsViewHolder;
import com.beak.bweibo.widget.holder.MenuItemHolder;

/**
 * Created by gaoyunfei on 15/7/28.
 */
public class MenuAdapter extends DelegateAdapter {

    private MenuItem.OnMenuItemClickListener mMenuItemClickListener = null;

    public MenuAdapter(Context context) {
        super(context);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, DelegateType delegateType, View view) {
        DelegateType type = DelegateType.getDelegateTypeById(viewType);
        switch (type) {
            case MENU_ITEM:
                return new MenuItemHolder (view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AbsViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuItemClickListener != null) {
                    AbsDelegate delegate = getDataItem(position);
                    if (delegate instanceof MenuItemDelegate) {
                        MenuItemDelegate menuItemDelegate = (MenuItemDelegate)delegate;
                        mMenuItemClickListener.onMenuItemClick(menuItemDelegate.getSource());
                    }
                }
            }
        });
    }

    public void setOnMenuItemClickListener (MenuItem.OnMenuItemClickListener listener) {
        mMenuItemClickListener = listener;
    }
}
