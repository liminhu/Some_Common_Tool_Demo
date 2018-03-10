package com.lmhu.advancelight.book.chapter3.CustomView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lmhu.advancelight.book.R;

public class CustomViewActivity extends AppCompatActivity {
   private InvalidTextView iv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        iv_text=(InvalidTextView)findViewById(R.id.iv_text_customview);
        iv_text.setText("hulimin");
    }
}
