package com.my.test.download.MyDownload;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.utils.MyLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;


public class DownloadManage {
    private Map map=new HashMap();
    private List<String> urlList=new ArrayList<>();



    public void getDataToMap(String downloadUrl, final String path){
        map.put(downloadUrl, path);
        urlList.add(downloadUrl);
    }


    public void download() {
        MyLog.e("开始下载。。。");
        if(urlList.size()==0){
            return;
        }
        final String url=urlList.remove(0);
       final String path=(String)map.get(url);
        if(TextUtils.isEmpty(path)){
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        Call call = okHttpClient.newCall(builder.build());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressStart(long totalBytes) {
                        super.onUIProgressStart(totalBytes);
                        Log.e("TAG", "onUIProgressStart:" + totalBytes);
                       // Toast.makeText(getApplicationContext(), "开始下载：" + totalBytes, Toast.LENGTH_SHORT).show();
                        //NewGGToast.showBottomToast("开始下载...");
                    }

                    @Override
                    public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                            MyLog.e("numBytes : "+numBytes +" -- totalBytes: "+totalBytes +"percent : "+percent);
                    }


                    @Override
                    public void onUIProgressFinish() {
                        super.onUIProgressFinish();
                       // Toast.makeText(getApplicationContext(), "结束下载", Toast.LENGTH_SHORT).show();
                        //NewGGToast.showBottomToast("结束下载...");
                    }
                });

                BufferedSource source = responseBody.source();
                File outFile = new File(path);
                outFile.delete();
                outFile.getParentFile().mkdirs();
                outFile.createNewFile();
                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                source.readAll(sink);
                sink.flush();
                source.close();

                MyLog.e("url -- "+response.request().url().encodedPath());
                map.remove(url);
                if(urlList.size()>0){
                    download();
                }
            }
        });

    }

}
