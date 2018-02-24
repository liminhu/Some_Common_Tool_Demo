package com.lmhu.advancelight.book.chapter10.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.databinding.model.Swordsman;
import com.lmhu.advancelight.book.databinding.ActivityBindingLayoutBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BindingLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_binding_layout);
        ActivityBindingLayoutBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_binding_layout);
        binding.setName("风清扬");
        binding.setAge(70);
        binding.setMan(false);

        ArrayList list=new ArrayList();
        list.add("0");
        list.add("1");
        binding.setList(list);


        Map map=new HashMap();
        map.put("age", "30");
        binding.setMap(map);

        String[] arrays={"张无忌","杨过"};
        binding.setArrays(arrays);

        Swordsman swordsman=new Swordsman("独孤求败","SS");
        binding.setSwordsman(swordsman);
        binding.setTime(new Date());
    }


}
