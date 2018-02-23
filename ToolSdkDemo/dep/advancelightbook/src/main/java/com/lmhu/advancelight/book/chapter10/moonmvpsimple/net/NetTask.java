package com.lmhu.advancelight.book.chapter10.moonmvpsimple.net;

import com.lmhu.advancelight.book.chapter10.moonmvpsimple.LoadTasksCallBack;

/**
 * Created by hulimin on 2018/2/23.
 */

public interface NetTask<T> {
    void execute(T data, LoadTasksCallBack callBack);
}
