package com.hlm.toolsdk.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.my.test.download.MyDownload.DownloadManage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import demo.toolsdk.hlm.com.toolsdkdemo.R;
import demo.utils.MyLog;


public class ServiceMainActivity extends AppCompatActivity implements View.OnClickListener{
    private  static  String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
    private static Activity activity;

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
 /*                   DownloadManage downloadManage=new  DownloadManage();
                    downloadManage.getDataToMap(url, "/sdcard/1.apk");
                    downloadManage.download();*/

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

        findViewById(R.id.share_picture).setOnClickListener(this);
        activity=this;
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

            case R.id.share_picture:
                File file=new File(Environment.getExternalStorageDirectory(),"1.jpg");
                String url=insertImageToSystem(this, file.getAbsolutePath(),"1.jpg");
                String path =url;
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/*");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(imageIntent, "分享"));

                Toast.makeText(this, "正在分享picture"+path,Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }



    private static String insertImageToSystem(Context context, String imagePath, String SHARE_PIC_NAME) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, SHARE_PIC_NAME, "desc");
            MyLog.e("url----"+url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MyLog.e("excep "+e.getMessage());
        }
        MyLog.e("file://"+ Environment.getExternalStorageDirectory()+imagePath);
      //  activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+imagePath)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(context, paths, null, null);
        } else {
            final Intent intent;
            File file=new File(url);
            if (file.isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                MyLog.e("directory changed, send broadcast:" + intent.toString());
            } else {
                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(new File(url)));
               MyLog.e("file changed, send broadcast:" + intent.toString());
            }
            context.sendBroadcast(intent);
        }

        return url;
    }


}
