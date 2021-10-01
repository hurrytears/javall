package com.apachee.thread;

/*
线程安全
买票案例


解决方法：
1.同步代码块
2.同步方法
3.锁机制, lock() , unlock()
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class RunnableImpl01 implements Runnable {
    private int ticket = 100;

    //创建一个锁对象
    Object obj = new Object();

    //成员变量创建锁对象，使用lock锁机制
    Lock l = new ReentrantLock();

    @Override
    public void run() {
        while (true) {

            //同步代码块
//            synchronized (obj) {
//                if (ticket > 0) {
//                    System.out.println(Thread.currentThread().getName() + "正在卖 " + ticket);
//                    ticket--;
//                }
//            }
            //调用同步方法，锁对象就是this，此处的this指的是文件对象
            sell();

            //锁机制
            l.lock();
            l.unlock();
        }
    }

    //同步方法
    /*static 静态方法， 锁对象不是this, 是class文件对象 */ synchronized void sell(){
        //或者synchronized(this)
        //静态：synchronized(RunnableImpl.class)
        if (ticket > 0) {
            System.out.println(Thread.currentThread().getName() + "正在卖 " + ticket);
            ticket--;
        }
    }
}


public class Demo02 {

    public static void main(String[] args) {
        RunnableImpl01 run = new RunnableImpl01();
        Thread t0 = new Thread(run);
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t0.start();
        t1.start();
        t2.start();
    }
}
