package com.lmhu.advancelight.book.chapter3.CustomProgressView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lmhu.advancelight.book.R;

import demo.utils.MyLog;

public class DotAlternateMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.e("onCrate --- "+getClass().getSimpleName());
        setContentView(R.layout.activity_dot_alternate_main);
    }
}
