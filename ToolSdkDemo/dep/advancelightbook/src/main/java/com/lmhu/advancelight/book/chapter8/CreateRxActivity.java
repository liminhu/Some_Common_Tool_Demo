package com.lmhu.advancelight.book.chapter8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lmhu.advancelight.book.R;

import java.util.concurrent.TimeUnit;

import demo.utils.MyLog;
import rx.Observable;
import rx.functions.Action1;

public class CreateRxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rx);
        repeat();

        range();

        interval();
    }

    private void repeat() {
        Observable.range(0,3)
                .repeat(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        MyLog.e("rx_repeat"+integer.intValue());
                    }
                });
    }

    private void range(){
        Observable.range(0,5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        MyLog.e("rx_range:"+integer.intValue());
                    }
                });
    }


    private void interval(){
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        MyLog.e("rx_interval:"+aLong.intValue());
                    }
                });
    }





}
