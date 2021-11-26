package com.apachee.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NioTest13 {

    // 编解码
    public static void main(String[] args) throws Exception {
        String inputFile = "data/test/NioTest14_in.txt";
        String outputFile = "data/test/NioTest14_out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputRandomAccessFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputRandomAccessFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputRandomAccessFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);

        Charset charset = Charset.forName("utf-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(inputData);

        ByteBuffer byteBuffer = encoder.encode(charBuffer);

        outputRandomAccessFileChannel.write(byteBuffer);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }
}
