package com.lmhu.advancelight.book.chapter6;

/**
 * Created by hulimin on 2018/1/7.
 */
// TODO: 2018/1/7 懒汉模式 线程不安全
public class Singleton_2 {
    private static Singleton_2 instance;

    private Singleton_2() {
    }

    public static Singleton_2 getInstance(){
        if(instance==null){
            instance=new Singleton_2();
        }
        return instance;
    }
}
