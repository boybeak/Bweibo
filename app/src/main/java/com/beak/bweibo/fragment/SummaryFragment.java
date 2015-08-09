package com.beak.bweibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beak.bweibo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gaoyunfei on 15/7/20.
 */
public class SummaryFragment extends Fragment {

    private SummaryProvider mSummary = null;

    @InjectView(R.id.summary_title)
    TextView mSummaryTitleTv = null;
    @InjectView(R.id.summary_sub_title)
    TextView mSummarySubTitleTv = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setSummaryProvider (SummaryProvider provider) {
        mSummary = provider;
        fillSummary(provider);
    }

    private void fillSummary (SummaryProvider provider) {
        mSummaryTitleTv.setText(provider.getTitle());
        mSummarySubTitleTv.setVisibility(TextUtils.isEmpty(provider.getSubTitle()) ? View.GONE : View.VISIBLE);
        mSummarySubTitleTv.setText(provider.getSubTitle());
    }

    public interface SummaryProvider {
        public CharSequence getTitle ();
        public CharSequence getSubTitle ();
        public CharSequence getContent();
        public String getThumbnailProfile ();
    }
}
