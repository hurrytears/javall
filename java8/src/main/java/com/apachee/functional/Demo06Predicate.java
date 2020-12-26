package com.apachee.functional;

import java.util.ArrayList;
import java.util.function.Predicate;

/*
作用：对某种数据类型的数据进行判断，结果返回一个boolean类型
boolean test(T t) 用来对指定数据类型数据进行判断的方法
 */
public class Demo06Predicate {

    static boolean checkString(String s, Predicate<String> pre){
        return pre.test(s);
    }

    static boolean checkString2(String s, Predicate<String> pre1, Predicate<String> pre2){
        return pre1.and(pre2).or(pre1).negate().test(s);
    }

    static ArrayList<String> filter(String[] arr, Predicate<String> pre1, Predicate<String> pre2){
        ArrayList<String> list = new ArrayList<>();
        for (String s : arr) {
            boolean b = pre1.and(pre2).test(s);
            if(b){
                list.add(s);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String s = "Hello";
        boolean res = checkString(s, (String str)->{
            return str.length()>5;
        });
        System.out.println(res);
        System.out.println("---------------------------");
        boolean res2 = checkString2(s, (t)->s.length()>5, (t)->t.contains("a"));
        System.out.println(res2);
        System.out.println("-----------------------------");
        String[] arr = {"迪丽热巴,女", "古力娜扎,女", "马儿扎哈,男"};
        ArrayList<String> list = filter(arr,
                (t) -> t.split(",")[1].equals("女"),
                (t) -> t.split(",")[0].length() == 4);
        for (String s1 : list) {
            System.out.println(s1);
        }
    }
}
