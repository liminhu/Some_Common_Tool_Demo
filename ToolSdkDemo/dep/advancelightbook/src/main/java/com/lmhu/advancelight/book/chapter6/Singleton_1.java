package com.lmhu.advancelight.book.chapter6;

/**
 * Created by hulimin on 2018/1/7.
 */
// TODO: 2018/1/7  1.饿汉模式
public class Singleton_1 {
    private static Singleton_1 instance=new Singleton_1();

    private Singleton_1() {
    }

    public static Singleton_1 getInstance(){
        return instance;
    }
}
