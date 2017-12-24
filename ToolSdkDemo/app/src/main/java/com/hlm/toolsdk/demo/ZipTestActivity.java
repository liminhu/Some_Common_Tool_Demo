package com.hlm.toolsdk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import demo.toolsdk.hlm.com.toolsdkdemo.R;
import demo.utils.MyLog;
import demo.zip.archiver.ArchiverManager;
import demo.zip.archiver.IArchiverListener;

public class ZipTestActivity extends Activity {
    private Button mButton, mDoZipButton;


    private String doZipSource= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"2"+File.separator;


    private String source= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"饥荒.zip";
    private String destPath=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"解压饥荒"+File.separator;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyLog.e("test .... ");
            String data=(String)msg.obj;
            switch (msg.what){
                case 1:
                    Toast.makeText(ZipTestActivity.this, data, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressDialog dialog=new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("解压中，请稍候。。。");
        dialog.setTitle("解压文件");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        final ArchiverListener listener=new ArchiverListener(dialog);

        mDoZipButton=(Button)findViewById(R.id.btn_dozip);
        mDoZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e("onclick ..." );
                ArchiverManager.getInstance(handler).doZipArchiver(doZipSource,"", true, "");
            }
        });

        mButton=(Button)findViewById(R.id.btn_unzip);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e("onclick ..." );
                ArchiverManager.getInstance(handler).doUnArchiver(source,destPath,"", listener);
            }
        });
    }




    private class ArchiverListener implements IArchiverListener{
        private ProgressDialog dialog;

        public ArchiverListener(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onStartArchiver() {
            MyLog.e("onStartArchiver 000 ");
            if(dialog!=null){
                dialog.show();
            }
        }

        @Override
        public void onProgressArchiver(int current, int total) {
            MyLog.e("onProgressArchiver 111 ");
             dialog.setMax(total);
             dialog.setProgress(current);
        }

        @Override
        public void onEndArchiver() {
            MyLog.e("onEndArchiver 2222 ");
             dialog.dismiss();
        }
    }
}
