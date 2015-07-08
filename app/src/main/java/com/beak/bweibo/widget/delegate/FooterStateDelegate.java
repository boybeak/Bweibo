package com.beak.bweibo.widget.delegate;

import android.content.Context;

import com.beak.bweibo.R;
import com.beak.bweibo.openapi.models.FooterState;

/**
 * Created by gaoyunfei on 15/5/28.
 */
public class FooterStateDelegate extends BaseDelegate<FooterState> {

    public FooterStateDelegate(FooterState footerState) {
        super(footerState);
    }

    @Override
    public DelegateType getDelegateType() {
        return DelegateType.SYSTEM_LIST_FOOTER;
    }

    public String getTip (Context context) {
        String tip = null;
        switch (getSource().getState()) {
            case FooterState.STATE_SUCCESS:
                tip = context.getString(R.string.footer_tip_success);
                break;
            case FooterState.STATE_FAILED:
                tip = context.getString(R.string.footer_tip_failed);
                break;
            case FooterState.STATE_LOADING:
                tip = "";
                break;
            case FooterState.STATE_NONE:
                tip = "";
                break;
        }
        return tip;
    }
}
