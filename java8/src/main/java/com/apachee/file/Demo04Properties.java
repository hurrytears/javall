package com.apachee.file;

//属性集
/*
Properties 是唯一和IO流相结合的集合
Properties可以使用store方法将集合中的临时数据持久化写入到硬盘中存储
可以使用load方法可以把硬盘中保存的键值对读取到集合中使用

默认键和值都是字符串

 */

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class Demo04Properties {

    public static void main(String[] args) throws IOException {
//        show01();
//        show02();
        show03();
    }

    /*
    void load(InputStream is);
    void load(Reader reader);
     */
    private static void show03() throws IOException {
//        FileInputStream fis = new FileInputStream("localdata/java/FileOutputStream.txt");
        FileReader fis = new FileReader("localdata/java/FileOutputStream.txt");
        Properties prop = new Properties();
        prop.load(fis);
        fis.close();
        Set<String> keys = prop.stringPropertyNames();
        for (String k: keys){
            System.out.println(k + ": " + prop.getProperty(k));
        }
    }

    /*
    void store(OutputStream out, String comments)
    void store(Writer writer, String comments)
    参数：
        OutputStream out: 字节输出流，不能写中文
        Writer write: 字符输出流，可以写中文
        String comments: 默认使用unicode编码，使用中文会乱码

     */
    private static void show02() throws IOException {
        Properties prop = new Properties();
        prop.setProperty("Hello", "1");
        prop.setProperty("Word", "张许东");

        FileWriter fw = new FileWriter("localdata/java/FileOutputStream.txt");
        prop.store(fw, "save data");
        fw.close();

//        FileOutputStream fos = new FileOutputStream("localdata/java/FileOutputStream.txt");
//        prop.store(fos, "hello");
//        fos.close();
    }


    private static void show01() {
        Properties prop = new Properties();
        prop.setProperty("Hello", "1");
        prop.setProperty("Word", "2");
        // prop.put(1, True);

        System.out.println(prop.getProperty("Hello"));

        Set<String> keys = prop.stringPropertyNames();
        for(String k: keys){
            System.out.println(k + ": " + prop.getProperty(k));
        }
    }
}
