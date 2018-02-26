package com.lmhu.advancelight.book.chapter10.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.databinding.model.ObFSwordsman;
import com.lmhu.advancelight.book.chapter10.databinding.model.ObSwordsman;
import com.lmhu.advancelight.book.chapter10.databinding.model.Swordsman;
import com.lmhu.advancelight.book.databinding.ActivityBindingUpdateBinding;


public class BindingUpdateActivity extends AppCompatActivity {
    private ActivityBindingUpdateBinding binding;
    private ObservableArrayList<Swordsman> list;
    private ObSwordsman obSwordsman;
    private Swordsman swordsman1;
    private Swordsman swordsman2;
    private ObFSwordsman obFSwordsman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_binding_update);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_binding_update);
        list=new ObservableArrayList<>();
        obSwordsman=new ObSwordsman("hulimin","A");
        binding.setObswordsman(obSwordsman);

        obFSwordsman=new ObFSwordsman("风清扬","S");
        binding.setObfswordsman(obFSwordsman);

        swordsman1=new Swordsman("张无忌","S");
        swordsman2=new Swordsman("周芷若","B");

        list.add(swordsman1);
        list.add(swordsman2);
        binding.setList(list);


        binding.btUpdateObswordsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obSwordsman.setName("东方不败");
            }
        });


        binding.btUpdateObfsswordsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obFSwordsman.name.set("令狐冲");
            }
        });


        binding.btUpdateObmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swordsman1.setName("杨过");
                swordsman2.setName("小龙女");
                list.add(swordsman1);
            }
        });

        binding.btUpdateBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obSwordsman.setName("任我行");
            }
        });

    }
}
