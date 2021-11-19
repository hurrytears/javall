package com.apachee.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest8 {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("data/test/NioTest2.txt");
        FileOutputStream fos = new FileOutputStream("data/test/NioTest8.text");

        FileChannel inputChannel = fis.getChannel();
        FileChannel outputChannel = fos.getChannel();

        // 专注于这个方法 零拷贝下
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);

        while (true){
            buffer.clear(); // if comment this line, what will be?

            int read = inputChannel.read(buffer);

            System.out.println(read);

            if(-1 == read) break;

            buffer.flip();

            outputChannel.write(buffer);
        }

        fis.close();
        fos.close();

    }
}
