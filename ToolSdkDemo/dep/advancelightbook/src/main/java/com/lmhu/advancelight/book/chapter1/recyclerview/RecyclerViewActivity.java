package com.lmhu.advancelight.book.chapter1.recyclerview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;

import java.util.ArrayList;
import java.util.List;

import demo.utils.MyLog;

public class RecyclerViewActivity extends AppCompatActivity {
    private List<String> mList;
    private RecyclerView recyclerView;
    private HomeAdapter mHomeAdaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initData();
        initView();
    }


    private void initData() {
        mList=new ArrayList<String>();
        for(int i=0; i<20; i++){
            mList.add(i+"");
        }
    }

    private void initView() {
        recyclerView=(RecyclerView)findViewById(R.id.id_recyclerview);
        setGridView();

    }

    private void setGridView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeAdaper=new HomeAdapter(this, mList);
        setLister();
        recyclerView.setAdapter(mHomeAdaper);
    }




    private void setLister(){
        mHomeAdaper.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyLog.e("position  : "+position+" -----");
                Toast.makeText(RecyclerViewActivity.this, "点击第 "+(position +1)+"条", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(RecyclerViewActivity.this)
                        .setTitle("确认删除吗？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyLog.e("position  : "+position+" --- i--"+i);
                                mHomeAdaper.removData(position);
                            }
                        }).show();
            }
        });
    }





}
