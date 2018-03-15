package com.lmhu.advancelight.book.chapter5.Okhttp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.lmhu.advancelight.book.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import rx.internal.util.ExceptionsUtils;

public class OkHttpEngineActivity extends AppCompatActivity {
    private static final String TAG="my_OKHttp3";
    private OkHttpClient okHttpClient;
    private Button bt_send;
    private Button bt_postsend;
    private Button bt_sendfile;
    private Button bt_downfile;
    private Button bt_cancel;

    //ScheduledExecutorService: 主要作用就是可以将定时任务与线程池功能结合使用
    private ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);
    private static final MediaType MEDIA_TYPE_MARKDOWN=MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG=MediaType.parse("image/png");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_engine);
    }
}
