package com.apachee.nio;

import java.nio.ByteBuffer;

public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(15);
        buffer.putChar('你');

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
    }
}
