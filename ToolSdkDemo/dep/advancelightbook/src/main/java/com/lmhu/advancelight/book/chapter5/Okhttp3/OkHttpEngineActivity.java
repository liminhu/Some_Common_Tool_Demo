package com.lmhu.advancelight.book.chapter5.Okhttp3;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import demo.utils.MyLog;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.internal.util.ExceptionsUtils;

public class OkHttpEngineActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "my_OKHttp3";
    private OkHttpClient okHttpClient;
    private Button bt_send;
    private Button bt_postsend;
    private Button bt_sendfile;
    private Button bt_downfile;
    private Button bt_cancel;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyLog.e("msg: " + msg.what);
            switch (msg.what) {
                case 0:
                    Toast.makeText(OkHttpEngineActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    //ScheduledExecutorService: 主要作用就是可以将定时任务与线程池功能结合使用
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_engine);
        initOkHttpClient();
        bt_send = (Button) this.findViewById(R.id.bt_send);
        bt_sendfile = (Button) this.findViewById(R.id.bt_sendfile);
        bt_postsend = (Button) this.findViewById(R.id.bt_postsend);
        bt_downfile = (Button) this.findViewById(R.id.bt_downfile);
        bt_cancel = (Button) this.findViewById(R.id.bt_cancel);
        bt_send.setOnClickListener(this);
        bt_postsend.setOnClickListener(this);
        bt_sendfile.setOnClickListener(this);
        bt_downfile.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);


        // Toast.makeText(OkHttpEngineActivity.this, "测试。。。", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_send) {
            // getAsynHttp();
            getAsynForEngine();
        } else if (i == R.id.bt_postsend) {
            postAsynHttp();
        }else if(i==R.id.bt_cancel){
            cancel();
        }
    }


    private void initOkHttpClient() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        okHttpClient = builder.build();
    }


    /**
     * get异步请求
     */
    private void getAsynHttp() {
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "请求失败";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                // MyLog.i(str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.e("请求成功。。。");
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = "请求成功！！！";
                        handler.sendMessage(msg);
                    }
                });

            }
        });
    }


    private void getAsynForEngine() {
        OkHttpEngine.getInstance(OkHttpEngineActivity.this).getAsynHttp("http://www.baidu.com", new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "请求失败";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(String str) throws IOException {
                //   MyLog.d("请求成功！！！");
                MyLog.d(str);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "请求成功！！！";
                handler.sendMessage(msg);
            }
        });
    }


    /**
     * psot异步请求
     */
    private void postAsynHttp() {
        RequestBody formBody = new FormBody.Builder()
                .add("ip", "59.108.54.37").build();
        Request request = new Request.Builder().url("http://ip.taobao.com/service/getIpInfo.php")
                .post(formBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "请求失败";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                MyLog.d(str);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "请求成功！！！" + str;
                handler.sendMessage(msg);
            }
        });
    }


    /**
     * 异步上传文件
     */
    private void postAsynFile() {
        String filePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            filePath = getFilesDir().getAbsolutePath();
        }
        MyLog.e("getFilesDir().getAbsolutePath(); --- " + getFilesDir().getAbsolutePath());
        File file = new File(filePath, "hlm.txt");
        final Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = request.body().toString();
                MyLog.e("data --- " + data);
            }
        });
    }


    /**
     * 异步下载文件
     */
    private void downAsynFile() {
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "文件下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                String filepath = "";
                try {
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    } else {
                        filepath = getFilesDir().getAbsolutePath();
                    }
                    File file = new File(filepath, "wangshu.jpg");
                    if (null != file) {
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "文件存储成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "文件存储失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMultipart() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }


    private void cancel() {
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .cacheControl(CacheControl.FORCE_CACHE).build();
        Call call = null;
        call = okHttpClient.newCall(request);
        final Call finalCall = call;
        MyLog.e("cancel ... ");
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                //finalCall.cancel();
            }
        }, 100, TimeUnit.MILLISECONDS);
        call.equals(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
               MyLog.e(" fail ... ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if(null!=response.cacheResponse()){
                     String str=response.cacheControl().toString();
                     MyLog.e("cache --- "+str);
                 }else {
                     String str=response.networkResponse().toString();
                     MyLog.e("network --  "+str);
                 }
            }
        });

    }
}




