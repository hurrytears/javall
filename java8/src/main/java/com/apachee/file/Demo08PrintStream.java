package com.apachee.file;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Demo08PrintStream {

    public static void main(String[] args) throws FileNotFoundException {
//        show01();
        show02();
    }

    private static void show02() throws FileNotFoundException {
        System.out.println("我在控制台");

        PrintStream ps = new PrintStream("localdata/java/目的地是打印流");
        System.setOut(ps);
        System.out.println("我在打印流的目的地输出");
    }

    private static void show01() throws FileNotFoundException {
        PrintStream ps = new PrintStream("localdata/java/print.txt");
        //继承自父类的方法自动将输出转换为字节
        ps.write(97);
        ps.println(97);
        ps.close();
    }
}
