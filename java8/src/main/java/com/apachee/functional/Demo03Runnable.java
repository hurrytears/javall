package com.apachee.functional;

public class Demo03Runnable {

    public static void startThread(Runnable run){
        new Thread(run).start();
    }

    public static void main(String[] args){
        startThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "---> 启动了");
            }
        });

        startThread(()-> System.out.println(Thread.currentThread().getName() + "---> 启动了吗"));
    }
}
