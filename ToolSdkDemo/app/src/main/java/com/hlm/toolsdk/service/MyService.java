package com.hlm.toolsdk.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class MyService extends Service {
    private static  final String TAG="my_hook_";

    private DownloadBinder mBinder=new DownloadBinder();
    public MyService() {
    }

    class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d(TAG+"MyService", "startDownload executed");
        }

        public int getProgress(){
            Log.d(TAG+"MyService","getProgress executed");
            return 0;
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG+"MyService", "Oncreate executed");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG+"MyService", "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG+"MyService", "onDestory executed");
    }
}
