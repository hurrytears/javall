package com.apachee.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * position
 *
 * limit
 *
 * capacity
 */
public class NioTest3 {

    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("data/test/NioTest3.txt");
        FileChannel fc = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        byte[] message = "hello world, nihao".getBytes();

        for (int i = 0; i < message.length; i++) {
            buffer.put(message[i]);
        }

        buffer.flip();

        fc.write(buffer);

        fos.close();
    }
}
