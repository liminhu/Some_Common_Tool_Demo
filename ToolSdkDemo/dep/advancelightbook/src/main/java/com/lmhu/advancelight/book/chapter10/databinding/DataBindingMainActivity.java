package com.lmhu.advancelight.book.chapter10.databinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.UpdateAppearance;
import android.view.View;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.databinding.model.Swordsman;
import com.lmhu.advancelight.book.databinding.ActivityDataBindingMainBinding;


public class DataBindingMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingMainBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_data_binding_main);
        Swordsman swordsman=new Swordsman("张无忌", "522");
        binding.setSwordsman(swordsman);
        binding.btLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DataBindingMainActivity.this, BindingLayoutActivity.class);
                startActivity(intent);
            }
        });


        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DataBindingMainActivity.this, BindingUpdateActivity.class);
                startActivity(intent);
            }
        });


    }
}
