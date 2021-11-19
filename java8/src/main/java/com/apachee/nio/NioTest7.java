package com.apachee.nio;

import java.nio.ByteBuffer;

// readonly buffer
public class NioTest7 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i=0; i<buffer.capacity(); ++i){
            buffer.put((byte) i);
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());
    }
}
