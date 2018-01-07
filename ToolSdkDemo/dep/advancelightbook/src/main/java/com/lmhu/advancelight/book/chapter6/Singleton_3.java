package com.lmhu.advancelight.book.chapter6;

/**
 * Created by hulimin on 2018/1/7.
 */
// TODO: 2018/1/7 懒汉模式 线程安全
public class Singleton_3 {
    private static Singleton_3 instance;

    private Singleton_3() {
    }

    public static synchronized  Singleton_3  getInstance(){
        if(instance==null){
            instance=new Singleton_3();
        }
        return instance;
    }
}
