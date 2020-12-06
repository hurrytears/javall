package com.apachee.thread;

/*
线程创建的两种方式
1.继承Thread类
2.实现Runnable接口
*/

class MyThread01 extends Thread{
    public void run(){
        System.out.println(Thread.currentThread().getName());
    }
}
public class Demo01 {
    public static void main(String[] args) {


    }
}
