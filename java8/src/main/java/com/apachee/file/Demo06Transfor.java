package com.apachee.file;

import java.io.*;

public class Demo06Transfor {

    static String path = "localdata/java/transfor.txt";

    public static void main(String[] args) throws IOException {
        show01();
        show02();
    }

    private static void show02() throws IOException {
        InputStream is = new FileInputStream(path);
        Reader reader = new InputStreamReader(is, "utf8");
        int len = 0;
        char[] chars = new char[1024];
        while((len = reader.read(chars)) != -1){
            System.out.println(new String(chars, 0, len));
        }
        reader.close();
    }

    private static void show01() throws IOException {
        OutputStream os = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(os, "utf8");
        System.out.println(osw.getEncoding());
        osw.write("我爱我的中国，就像爱大海一样");
        osw.flush();
        osw.close();
    }
}
