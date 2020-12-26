package com.apachee.functional;

import java.util.function.Function;

//用于类型转换
public class Demo08Function {
    static void change(String s, Function<String, Integer> fun1, Function<Integer, String> fun2) {
        String res = fun1.andThen(fun2).apply(s);
        System.out.println(res);
    }

    static void change2(String s,
                        Function<String, String> fun1,
                        Function<String, Integer> fun2,
                        Function<Integer, Integer> fun3){
        int res = fun1.andThen(fun2).andThen(fun3).apply(s);
        System.out.println(res);
    }

    public static void main(String[] args) {
        String s = "1234";
        change(s, (t) -> Integer.parseInt(t) + 10, (t) -> t.toString());
        System.out.println("-------------------------------");
        String s1 = "赵丽颖,18";
        change2(s1, (t1) -> t1.split(",")[1],
                (t) -> Integer.parseInt(t),
                (t) -> t + 10);
    }
}
