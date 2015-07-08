package com.beak.bweibo.widget.holder;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beak.bweibo.R;
import com.beak.bweibo.openapi.models.FooterState;
import com.beak.bweibo.widget.delegate.FooterStateDelegate;

/**
 * Created by gaoyunfei on 15/5/28.
 */
public class ListFooterHolder extends AbsViewHolder<FooterStateDelegate> {

    public ProgressBar loadingProgressBar = null;
    public TextView tipTv = null;

    public ListFooterHolder(View itemView) {
        super(itemView);
        loadingProgressBar = (ProgressBar)itemView.findViewById(R.id.footer_progressbar);
        tipTv = (TextView)itemView.findViewById(R.id.footer_tip);
    }

    @Override
    public void bindData(Context context, FooterStateDelegate footerDelegate) {
        FooterState state = footerDelegate.getSource();
        tipTv.setText(footerDelegate.getTip(context));
        switch (state.getState()) {
            case FooterState.STATE_LOADING:
                loadingProgressBar.setVisibility(View.VISIBLE);
                tipTv.setVisibility(View.GONE);
                break;
            case FooterState.STATE_FAILED:
                loadingProgressBar.setVisibility(View.GONE);
                tipTv.setVisibility(View.VISIBLE);
                break;
            case FooterState.STATE_SUCCESS:
                loadingProgressBar.setVisibility(View.GONE);
                tipTv.setVisibility(View.VISIBLE);
                break;
            case FooterState.STATE_NONE:
                loadingProgressBar.setVisibility(View.GONE);
                tipTv.setVisibility(View.GONE);
                break;
        }
    }
}
