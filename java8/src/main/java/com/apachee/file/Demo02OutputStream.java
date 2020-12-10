package com.apachee.file;

/*
OutputStream: 输出字节流的所有类的超类

public void close(); 关闭并释放
public void flush(); 刷新输出流并且强制任何缓冲的输出字节被写出
public void write(byte[] b); 将b.length字节从指定的字节数组写入此输出流
public void write(byte[] b, int off, int len); 从指定的字节数组写入len字节，从偏移量off开始输出到此输出流
public void abstract void write(int b): 将指定的字节输出流

java.io. extends OutputStream
FileOutputStream: 文件字节输出流
作用：把内存中的数据写入到磁盘中

构造方法：
FileOutputStream(String name)
FileOutputStream(File file)

 */

import java.io.FileOutputStream;
import java.io.IOException;

public class Demo02OutputStream {

    public static void main(String[] args) throws IOException {
        FileOutputStream os = new FileOutputStream("localdata/java/FileOutputStream.txt");
//        os.write(new byte[3], 1, 3);

        os.write(97);
        //十进制的整数转换为二进制的证书
        os.close();
    }
}
