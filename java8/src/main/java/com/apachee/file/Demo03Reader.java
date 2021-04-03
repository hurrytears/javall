package com.apachee.file;


//Reader 字符输入流最顶层的父类

/*
java.io.FileReader extends InputStreamReader extends

追加续写，FileWriter(String filePath, boolean append)

注意jdk7 和 jdk9 中利用try catch 对IO流做的关闭优化，放弃了finally
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Demo03Reader {

    public static void main(String[] args) throws IOException {
//        reader();
        writer();
    }

    private static void writer() throws IOException {
        FileWriter fw = new FileWriter("localdata/java/FileWriter.txt");
        fw.write(97);
        fw.flush(); // 刷新缓冲区，流对象继续使用
        fw.write(98);
        char[] cs = {'a', 'b', 'c', 'd', 'e'};
        fw.write(cs, 2, 3);
        fw.write(" chuanzhiboke黑马程序员");
        fw.close();
//        fw.write(99);
    }

    static void reader() throws IOException {
        FileReader fr = new FileReader("localdata/java/FileOutputStream.txt");
//        int len = 0;
//        while ((len = fr.read()) != -1){
//            System.out.println((char)len);
//        }
        char[] cs = new char[1024];
        int len = 0;
        while ((len = fr.read(cs)) != -1){
            System.out.println(new String(cs, 0, len));
        }
    }
}
