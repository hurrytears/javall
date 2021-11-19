package com.apachee.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * java 1.4版本开始有的nio
 *
 * java.io 核心概念是流 Stream, 面向流的编程。java中，要么是输出流，要么是输入流。
 * java.nio 有三个核心概念：Selector, Channel, Buffer. 面向块 block 或是缓冲区 Buffer 编程
 *  Buffer 是一块内存，底层是数组, 数据的读和写都基于Buffer完成
 *
 *  除了数组之外，Buffer 还提供了结构化的数据访问方式，并且可以追踪到系统的读写过程
 *
 *  Channel指的是可以向其写入数据，或者从中读取数据的对象，类似与io 的stream, 所有数据的
 *  读写都是通过buffer进行的，永远不会出现直接从Channel读写的情况
 *
 *  与Stream不同的是，Channel是双向的，它能更好地反应出底层操作系统的真实情况
 *  比如Linux系统中底层操作系统的通道就是双向的
 *
 */
public class NioTest1 {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        for (int i=0; i<buffer.capacity(); ++i){
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        // 读和写之间的状态反转
        buffer.flip();

        while(buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
