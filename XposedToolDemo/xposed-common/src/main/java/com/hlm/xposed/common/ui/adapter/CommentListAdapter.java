
package com.hlm.xposed.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlm.xposed.common.ui.base.BaseListAdapter;
import com.hlm.xposed.common.ui.view.CommentItemView;


public class CommentListAdapter extends BaseListAdapter<String> {

    public CommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return new CommentItemView(getContext());
    }

    @Override
    public ViewHolder<String> onCreateViewHolder(View view, int viewType) {
        return new CommentViewHolder(view, this);
    }

    private final class CommentViewHolder extends ViewHolder<String> {

        CommentItemView mCommentItemView;

        public CommentViewHolder(View itemView, BaseListAdapter<String> baseListAdapter) {
            super(itemView, baseListAdapter);
        }

        @Override
        public void onInitialize() {
            super.onInitialize();

            mCommentItemView = (CommentItemView) mItemView;
        }

        @Override
        public void onBind(int position, int viewType) {

            // 设置内容
            mCommentItemView.setContent(getItem(position));
        }
    }
}
