package com.lmhu.advancelight.book.chapter1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lmhu.advancelight.book.R;

import demo.utils.MyLog;


public class MoonPermissionActivity extends AppCompatActivity {
    private Button bt_call;
    private Button bt_permissionsdispatcher;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone_permission);
        bt_call = (Button) findViewById(R.id.bt_call);
        bt_permissionsdispatcher = (Button) findViewById(R.id.bt_permissiondispatcher);
        bt_permissionsdispatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoonPermissionActivity.this, ThirdPartyActivity.class);
                startActivity(intent);
            }
        });

        bt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
    }

    private void call() {
        //检查App是否有permission.Call_phone权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }





    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        try {
            startActivity(intent);  //
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CALL_PHONE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPhone();
            }else {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){
                    MyLog.e("test ... ");
                    AlertDialog dialog=new AlertDialog.Builder(this)
                            .setMessage("该功能需要访问电话的权限， 不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).create();
                    dialog.show();
                }
                return;
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




    /*
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

    }*/


}
