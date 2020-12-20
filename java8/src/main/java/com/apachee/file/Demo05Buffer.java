package com.apachee.file;

import java.io.*;

public class Demo05Buffer {

    static String path = "localdata/java/buffer.txt";

    /*
    BufferedInputStream
    BufferedOutoutStream: 字节缓冲输出流
    BufferedReader
    BufferedWriter
     */

    public static void main(String[] args) throws IOException {
//        show01();
//        show02();
//        show03();
        show04();
    }

    private static void show04() throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
//        int len = 0;
//        while((len = br.read()) != -1){
//            System.out.println(len);
//        }
        String line = br.readLine();
        System.out.println(line);
        br.close();
    }

    private static void show03() throws IOException {
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("你好我是writer");
        bw.newLine();
        bw.write("你好我是writer");
        bw.close();
    }

    private static void show02() throws IOException {
        FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int len = 0;
        byte[] buf = new byte[1024];
//        while ((len = bis.read()) != -1){
//            System.out.println(len);
//        }

        while ((len = bis.read(buf)) != -1){
            System.out.println(new String(buf, 0, len));
        }
        bis.close(); //不用关闭字符流
    }

    private static void show01() throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write("我把数据写入到内部缓冲区中".getBytes());
        bos.flush();
        bos.close();
    }
}
