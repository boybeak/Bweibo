package com.beak.bweibo.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.beak.bweibo.openapi.models.FooterState;
import com.beak.bweibo.widget.delegate.AbsDelegate;
import com.beak.bweibo.widget.delegate.BaseDelegate;
import com.beak.bweibo.widget.delegate.DelegateType;
import com.beak.bweibo.widget.delegate.FooterStateDelegate;
import com.beak.bweibo.widget.holder.AbsViewHolder;
import com.beak.bweibo.widget.holder.ListFooterHolder;

import java.util.Collection;

/**
 * Created by gaoyunfei on 15/6/4.
 */
public abstract class FooterAdapter extends DelegateAdapter {

    private FooterState mFooterState = null;
    private FooterStateDelegate mFooterDelegate = null;

    public FooterAdapter(Context context) {
        super(context);
        mFooterState = new FooterState();
        mFooterDelegate = new FooterStateDelegate(mFooterState);
        addDataItem(0, mFooterDelegate);
    }

    @Override
    public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, DelegateType delegateType, View view) {
        if (delegateType == DelegateType.SYSTEM_LIST_FOOTER) {
            return new ListFooterHolder(view);
        }
        return null;
    }

    @Override
    public void addDataList(int position, Collection<? extends BaseDelegate> list) {
        super.addDataList(position, list);
        checkFooter();
    }

    @Override
    public void addDataList(Collection<? extends BaseDelegate> list) {
        super.addDataList(list);
        checkFooter();
    }

    @Override
    public void addDataItem(int position, BaseDelegate delegate) {
        super.addDataItem(position, delegate);
        checkFooter();
    }

    @Override
    public void setDataItem(int position, BaseDelegate delegate) {
        if (position < 0 || position >= getDataList().size()) {
            return;
        }
        if (getDataItem(position) == mFooterDelegate) {
            return;
        }
        super.setDataItem(position, delegate);
    }

    @Override
    public void addDataItem(BaseDelegate delegate) {
        super.addDataItem(delegate);
        checkFooter();
    }

    @Override
    public AbsDelegate removeDataItem(int position) {
        if (position < 0 || position >= getDataList().size()) {
            return null;
        }
        if (getDataItem(position) == mFooterDelegate) {
            return null;
        }
        return super.removeDataItem(position);
    }

    @Override
    public void removeDataItem(Object object) {
        if (object == mFooterDelegate) {
            return;
        }
        super.removeDataItem(object);
    }

    public void setFooterState (int state) {
        mFooterState.setState(state);
    }

    private void checkFooter () {
        if (containsFooter()) {
            getDataList().remove(mFooterDelegate);
        }
        getDataList().add(mFooterDelegate);
    }

    private boolean containsFooter () {
        return getDataList().contains(mFooterDelegate);
    }
}
