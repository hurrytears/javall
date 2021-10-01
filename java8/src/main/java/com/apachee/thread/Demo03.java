package com.apachee.thread;

/*
线程的状态
NEW
BLOCKED(阻塞)
RUNNABLE
TERMINATED(死亡状态)
WAITING(无限等待状态)
TIMED_WAITING(计时等待)

等待唤醒案例，包子铺
 */


public class Demo03 {
    public static void main(String[] args) {
        Object obj = new Object();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (obj) {
                        System.out.println("告知老板要的包子种类和数量");
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //唤醒之后执行的代码
                        System.out.println("包子已经做好了，开吃");
                    }
                }
            }
        }.start();
        new Thread(() -> {
            while(true) {
                //花了5秒做包子
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj) {
                    System.out.println("老板5秒之后做好了包子，告知顾客可以开始吃了");
                    obj.notify();
                }
            }
        }).start();
    }
}
