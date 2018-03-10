package com.lmhu.advancelight.book.chapter8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lmhu.advancelight.book.R;

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_create;
    private Button bt_transform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        initView();
    }

    private void initView() {
        bt_create=(Button)findViewById(R.id.bt_create_rx);
        bt_transform=(Button)findViewById(R.id.bt_transform_rx);



        bt_create.setOnClickListener(this);
        bt_transform.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_create_rx) {
            Intent intent=new Intent(RxJavaActivity.this, CreateRxActivity.class);
            startActivity(intent);
        }
    }
}
