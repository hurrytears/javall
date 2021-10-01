package com.apachee.thread;

/*
线程创建的两种方式
1.继承Thread类
2.实现Runnable接口

实现Runnable接口的好处：
1.避免了单继承的局限性
2.增强了程序的扩展性，降低了耦合性：把定义线程和开启线程进行了分离


Thread的方法：
    设置线程的名称，构造方法和setName两种方法
    sleep() 暂停当前线程，需要捕获异常
*/

class MyThread01 extends Thread{
    public MyThread01(){
    }

    public MyThread01(String name){
        super(name);
    }

    public void run(){
        System.out.println(Thread.currentThread().getName());
    }
}
class MyThread02 implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
public class Demo01 {
    public static void main(String[] args) {
        Thread t1 = new MyThread01("a");
        t1.setName("b");
        Thread t2 = new Thread(new MyThread02(), "newName");
        t2.setName("c");
        t1.start();
        t2.start();
    }
}
