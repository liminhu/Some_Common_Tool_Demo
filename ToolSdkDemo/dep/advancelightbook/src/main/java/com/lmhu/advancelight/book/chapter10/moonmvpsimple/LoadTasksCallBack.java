package com.lmhu.advancelight.book.chapter10.moonmvpsimple;

/**
 * Created by hulimin on 2018/2/23.
 */

public interface LoadTasksCallBack<T> {
    void onSuccess(T t);
    void onStart();
    void onFailed();
    void onFinish();
}
