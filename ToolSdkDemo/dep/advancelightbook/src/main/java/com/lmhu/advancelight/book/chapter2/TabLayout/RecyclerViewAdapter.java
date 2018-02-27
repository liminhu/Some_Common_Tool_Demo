package com.lmhu.advancelight.book.chapter2.TabLayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo.IpInfoContract;

/**
 * Created by hulimin on 2018/2/26.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final View view=holder.mView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext," 奔跑在孤傲的路上",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
        return new ViewHolder(view);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
        }
    }
}
