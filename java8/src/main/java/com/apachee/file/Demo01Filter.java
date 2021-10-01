package com.apachee.file;

/*
boolean accept(File pathname)
File[] listFiles(FilenameFilter filter) 用于过滤文件名称

 */

import java.io.File;

public class Demo01Filter {
    public static void main(String[] args) {
        File file = new File("d:\\");
        getAllFile(file);
    }

    /*
    定义一个方法，参数传递File类型的目录
    方法中对目录进行遍历
     */
    public static void getAllFile(File dir){
        System.out.println(dir);
        File[] files = dir.listFiles();
        for(File f: files){
            if(f.isDirectory()){
                getAllFile(f);
            }else {
                System.out.println(f);
            }
        }
    }
}
