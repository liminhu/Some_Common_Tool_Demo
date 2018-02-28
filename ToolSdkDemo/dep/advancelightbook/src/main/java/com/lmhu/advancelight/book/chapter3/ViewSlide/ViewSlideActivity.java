package com.lmhu.advancelight.book.chapter3.ViewSlide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lmhu.advancelight.book.R;

public class ViewSlideActivity extends AppCompatActivity {
    private ViewSlideCustomView viewSlideCustomView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slide);
        viewSlideCustomView=(ViewSlideCustomView)findViewById(R.id.customview_slide);
        //使用Scroll来进行平滑移动
        viewSlideCustomView.smoothScrollTo(-400,0);
    }


}
