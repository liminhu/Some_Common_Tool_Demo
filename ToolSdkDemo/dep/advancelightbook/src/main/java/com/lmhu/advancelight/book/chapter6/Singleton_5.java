package com.lmhu.advancelight.book.chapter6;

/**
 * Created by hulimin on 2018/1/7.
 */
// TODO: 2018/1/7 静态内部类单例模式 -- 推荐使用
public class Singleton_5 {
    private Singleton_5() {
    }

    public static Singleton_5 getInstance(){
        return SingletonHolder.sInstance;
    }


    private static class SingletonHolder{
        private static final Singleton_5 sInstance=new Singleton_5();
    }
}

