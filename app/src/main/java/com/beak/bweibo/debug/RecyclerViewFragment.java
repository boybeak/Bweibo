package com.beak.bweibo.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by gaoyunfei on 15/6/5.
 */
public class RecyclerViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DrawableAdapter());
    }

    private class DrawableAdapter extends RecyclerView.Adapter<DrawableHolder> {

        private Field[] mFields = android.support.v7.appcompat.R.drawable.class.getFields();

        @Override
        public DrawableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView iv = new ImageView(getActivity());
            return new DrawableHolder(iv);
        }

        @Override
        public void onBindViewHolder(DrawableHolder holder, int position) {
            try {
                holder.iv.setImageResource(mFields[position].getInt(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mFields.length;
        }
    }

    private class DrawableHolder extends RecyclerView.ViewHolder {

        public ImageView iv = null;

        public DrawableHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView;
        }
    }
}
