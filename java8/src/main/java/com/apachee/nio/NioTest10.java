package com.apachee.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class NioTest10 {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("data/test/NioTest2.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        FileLock lock = channel.lock(3, 6, true);

        System.out.println("valid" + lock.isValid());
        System.out.println("valid type" + lock.isShared());

        lock.release();

        randomAccessFile.close();
    }
}
