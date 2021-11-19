package com.apachee.nio;

import java.nio.ByteBuffer;

// slice 用法，底层共享数组
public class NioTest6 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for(int i=0; i<buffer.capacity(); ++i){
            buffer.put((byte)i);
        }

        buffer.position(2);
        buffer.limit(6);

        ByteBuffer slice = buffer.slice();

        for(int i=0; i<slice.capacity(); ++i){
            byte b = slice.get(i);
            b *= 2;
            slice.put(i, b);
        }

        buffer.clear();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
