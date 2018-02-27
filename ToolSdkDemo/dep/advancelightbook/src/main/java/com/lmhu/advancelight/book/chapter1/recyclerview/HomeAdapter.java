package com.lmhu.advancelight.book.chapter1.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lmhu.advancelight.book.R;

import java.util.List;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/26.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>implements View.OnClickListener, View.OnLongClickListener{
    private List<String> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public HomeAdapter(Context mContext, List<String> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removData(int position){
        MyLog.e("mlist size -- "+mList.size());
        for(int i=0; i<mList.size(); i++){
            if(mList.get(i).equals(position+"")){
                mList.remove(i);
            }
        }
        notifyItemRemoved(position);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }







    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(view, (int)view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(onItemClickListener!=null){
            onItemClickListener.onItemLongClick(view, (int)view.getTag());
        }
        return true;
    }















    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.tv_item_recycler);
        }
    }
}
