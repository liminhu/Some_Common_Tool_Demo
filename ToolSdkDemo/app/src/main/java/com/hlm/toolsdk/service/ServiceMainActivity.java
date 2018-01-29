package com.hlm.toolsdk.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.my.test.download.MyDownload.DownloadManage;

import demo.toolsdk.hlm.com.toolsdkdemo.R;
import demo.utils.MyLog;


public class ServiceMainActivity extends AppCompatActivity implements View.OnClickListener{
    private  static  String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";

    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(MyService.DownloadBinder)service;
            Log.d("hook_MainActivity", "Thread id is "+ Thread.currentThread().getId()+"---  111");

            downloadBinder.startDownload();
            Log.d("hook_MainActivity", "Thread id is "+ Thread.currentThread().getId()+"---  222");
            downloadBinder.getProgress();

            new Thread(){
                @Override
                public void run() {

                    DownloadManage downloadManage=new  DownloadManage();
                    downloadManage.getDataToMap(url, "/sdcard/1.apk");
                    downloadManage.download();

                }
            }.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("hook_MainActivity", "Thread id is "+ Thread.currentThread().getId()+"---  333");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);
        Button startService=(Button)findViewById(R.id.service_start);
        Button stopService=(Button)findViewById(R.id.service_stop);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        Button bindService=(Button)findViewById(R.id.bind_service);
        Button unBindService=(Button)findViewById(R.id.unbind_service);
        bindService.setOnClickListener(this);
        unBindService.setOnClickListener(this);
        Button startIntentService=(Button)findViewById(R.id.Start_intent_service);
        startIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_start:
                MyLog.e(" start service_start ");
                Intent startIntent=new Intent(this, MyService.class);
                startService(startIntent); //启动服务
                break;

            case R.id.service_stop:
                MyLog.e(" stop service_start ");
                Intent stopIntent=new Intent(this, MyService.class);
                stopService(stopIntent);
                break;

            case R.id.bind_service:
                MyLog.e(" start bind_service ");
                Intent bindIntent=new Intent(this, MyService.class);
                //绑定服务
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                Log.d("my_hook_MainActivity", "Thread id is unbind_service ");
                unbindService(connection);
                break;
            default:
                break;
        }
    }




}
