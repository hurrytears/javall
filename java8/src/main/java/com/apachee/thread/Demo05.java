package com.apachee.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
线程池
jdk 1.5 之后
java.util.concurrent.Excutors
submit(Runnable task)
void shutdown()
 */
public class Demo05 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        Future<?> submit = pool.submit(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        Thread.sleep(5);
        System.out.println(submit.isDone());
        pool.shutdown();
    }
}
