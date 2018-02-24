package com.lmhu.advancelight.book.chapter10.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    }
}
