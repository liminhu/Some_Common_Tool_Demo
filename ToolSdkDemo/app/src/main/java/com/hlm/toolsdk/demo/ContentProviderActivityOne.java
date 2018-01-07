package com.hlm.toolsdk.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import demo.toolsdk.hlm.com.toolsdkdemo.R;
import demo.utils.MyLog;

public class ContentProviderActivityOne extends AppCompatActivity implements View.OnClickListener{
    private Button callProviderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
        callProviderBtn=(Button)findViewById(R.id.testCallProvicer);
        callProviderBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.testCallProvicer:
                MyLog.e("test ...call  "+callProviderBtn.getText());
                Toast.makeText(this, "this is a test ... call ", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
