package com.lmhu.advancelight.book.chapter1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import demo.utils.ResourceUtil;


public class MoonPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_moon_permission);

        int test_permission= ResourceUtil.getLayoutId(this, "activity_moon_permission");
        setContentView(test_permission);
        int view_id= ResourceUtil.getId(this, "Real_testCallProvicer");
        Button button=(Button) findViewById(view_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "content://com.hlm.toolsdk.demo.callProvider/#";
                String data="";
                try{
                    Bundle bundle = getContentResolver().call(Uri.parse(uri), "test", null, null);
                    data=bundle.getString("data");
                    if(!TextUtils.isEmpty(data)){
                        data+=uri;
                    }
                }catch (Exception e){
                    data=uri+"-- bundle is null ....";
                }
                Toast.makeText(MoonPermissionActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
