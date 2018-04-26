package com.hlm.douyin.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mx.sdk.nativeutils.NativeUtils;

import demo.toolsdk.hlm.com.webviewlib.R;
import demo.utils.MyLog;

public class DouyinMainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_main);
        button=(Button)findViewById(R.id.dy_bt);
        button.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==button.getId()){
            String  ddd=NativeUtils.nativeD(1, "test");
            MyLog.e(" --- 000 "+ddd);
            Toast.makeText(this," click test ", Toast.LENGTH_SHORT).show();
        }
    }



}
