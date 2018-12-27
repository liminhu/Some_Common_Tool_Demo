package com.test.hlm.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdk.hlm.tool.utils.MyLog;
import com.sdk.hlm.tool.utils.NativeUtils;

public class MainActivity extends AppCompatActivity {
    private static final String LIB_NAME= "sdk-plugins";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.e("test checkUpgrade..  ");
      //  Beta.checkUpgrade();
      //  BugleConfig.checkUpgrade();

        System.loadLibrary(LIB_NAME);
        setContentView(R.layout.activity_main);
        TextView tv=(TextView)findViewById(R.id.click);
        tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NativeUtils.nativeGetFuncPtr(1,0);
               // Intent intent=new Intent(MainActivity.this, SecondActivity.class);
               // startActivity(intent);
                Toast.makeText(getApplicationContext(), "click test !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
