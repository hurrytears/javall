package com.apachee.thread;

/*
进入到TimeWaiting有两种方式
1.sleep(long s)
2.wait(long s)

notfileall 唤醒同一个锁对象的所有wait线程

线程间通信，生产一个包子，吃一个包子
生产一个变更包子状态，吃一个变更包子状态
 */

class Baozi {
    String pi, xian;
    Boolean flag = false;
}

class Baozipu extends Thread {
    private Baozi bz;

    public Baozipu(Baozi baozi) {
        this.bz = baozi;
    }

    public void run() {
        int count = 0;
        while (true) {
            synchronized (bz) {
                bz.notify();  //这一行的位置无关紧要
                //包子铺等待吃货下单
                if(bz.flag == true) {
                    try {
                        bz.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //被唤醒后开始做包子
                if (count % 2 == 0) {
                    bz.pi = "薄皮";
                    bz.xian = "三鲜";
                } else {
                    bz.pi = "冰皮";
                    bz.xian = "牛肉";
                }
                count++;
                System.out.println("包子铺正在生产" + bz.pi + bz.xian + "包子");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bz.flag = true;

                System.out.println("包子铺已经做好了" + bz.pi + bz.xian + "包子，吃货可以开始吃了");
            }
        }
    }

}
class Chihuo extends Thread{
    private Baozi bz;
    public Chihuo(Baozi bz){
        this.bz = bz;
    }

    @Override
    public void run() {
        while (true){
            synchronized (bz){
                if(bz.flag == false){
                    try {
                        bz.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                //被唤醒之后开始吃包子
                System.out.println("吃货正在吃" + bz.pi + bz.xian + "包子");
                bz.flag = false;
                bz.notify();
                System.out.println(bz.pi + bz.xian + "包子已经被吃完了");
                System.out.println("------------------------------------------");
            }
        }
    }
}

public class Demo04 {
    public static void main(String[] args) {
        Baozi bz = new Baozi();
        new Baozipu(bz).start();
        new Chihuo(bz).start();
    }
}
