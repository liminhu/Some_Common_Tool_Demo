package com.lmhu.advancelight.book.chapter6;

/**
 * Created by hulimin on 2018/1/7.
 */
// TODO: 2018/1/7 双重检查模式 DCL
public class Singleton_4 {
    //Volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变化时，
    // 强迫线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。
    private volatile static Singleton_4 instance;

    private Singleton_4() {
    }

    public static Singleton_4 getInstance(){
        if(instance==null){
            synchronized (Singleton_4.class){
                if(instance==null){
                    instance=new Singleton_4();
                }
            }
        }
        return instance;
    }
}
