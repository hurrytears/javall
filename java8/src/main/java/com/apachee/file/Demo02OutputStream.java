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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Demo02OutputStream {

    public static void main(String[] args) throws IOException {
//        out();
        in();
    }

    static void out() throws IOException{
        FileOutputStream os = new FileOutputStream("localdata/java/FileOutputStream.txt");
//        os.write(new byte[3], 1, 3); 从第二个开始写三个

//        os.write(97);
//        os.write(new byte[]{49, 48, 48});

//        byte[] bytes1 = "你好".getBytes();
//        System.out.println(Arrays.toString(bytes1));

//        byte[] bytes = {-65,66,-67,68,69};
        byte[] bytes = {65,66,67,68,69};
        os.write(bytes);
        //十进制的整数转换为二进制的整数
        os.close();
    }

    static void in() throws IOException {
        FileInputStream fis = new FileInputStream("localdata/java/FileOutputStream.txt");

        byte[] bytes = new byte[2];
        int len = 0;

        while ((len = fis.read(bytes)) != -1){
            System.out.println(new String(bytes, 0, len));
        }

        fis.close();
    }

}
