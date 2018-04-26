package com.mx.sdk.nativeutils;

public  abstract class Singleton<T> {
    private T mInstance;

    protected abstract T createInstance();


    public final T getInstance() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = createInstance();
            }
            return mInstance;
        }
    }
}
