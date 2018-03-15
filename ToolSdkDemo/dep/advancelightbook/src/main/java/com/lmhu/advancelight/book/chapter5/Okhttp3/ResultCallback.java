package com.lmhu.advancelight.book.chapter5.Okhttp3;

import java.io.IOException;

import okhttp3.Request;

/**
 * Created by hulimin on 2018/3/7.
 */
public abstract class ResultCallback {
    public abstract void onError(Request request, Exception e);
    public abstract void onResponse(String str) throws IOException;
}
