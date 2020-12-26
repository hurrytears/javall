package com.apachee.functional;

import java.util.function.Consumer;
import java.util.function.Supplier;

//常用的函数类接口
/*
Supplier<T>  T get();
 */
public class Demo05SupplierAndConsumer {

    static String getString(Supplier<String> sup) {
        return sup.get();
    }

    static int getMax(Supplier<Integer> sup) {
        return sup.get();
    }

    static void method(String name, Consumer<String> con) {
        con.accept(name);
    }

    /*
    Consumer接口的默认方法andthen
    作用：需要两个Consumer接口，可以把两个Consumer接口组合到一起，对数据进行消费
    例如：
    Consumer<String> con1
    Consumer<String> con2
    String s = "Hello";
    con1.accept(s);
    con2.accept(s);
    连接两个Consumer接口  再进行消费
    con1.andThen(con2).accept(s)  谁先写谁先消费
     */
    static void method2(String s, Consumer<String> con1, Consumer<String> con2) {
        con1.accept(s);
        con2.accept(s);

        con1.andThen(con2).accept(s);
    }

    static void method3(String[] arr, Consumer<String> con1, Consumer<String> con2) {
        for (String s : arr) {
            con1.andThen(con2).accept(s);
        }
    }

    public static void main(String[] args) {
        //调用getString方法，方法的参数是个supplier是个函数式接口
        String s = getString(() -> "胡歌");
        System.out.println(s);

        //数组中元素的最大值
        int[] arr = {100, 0, -50, 88, 99, 33, -30};
        int maxvalue = getMax(() -> {
            int max = arr[0];
            for (int i : arr) {
                if (i > max) {
                    max = i;
                }
            }
            return max;
        });
        System.out.println(maxvalue);

        //Consumer接口，消费数据
        method("张三", (name) -> System.out.println(new StringBuilder(name).reverse().toString()));

        //andThen
        method2("Hello", (t) -> System.out.println(t.toUpperCase()), (t) -> System.out.println(t.toLowerCase()));

        //实例
        String[] arr2 = {"迪丽热巴,女", "古力娜扎,女", "马儿扎哈,男"};
        method3(arr2, (msg)->{
            //消费方式，对msg进行切割
            String name = msg.split(",")[0];
            System.out.print("姓名：" + name);
        }, (msg)->{
            String sex = msg.split(",")[1];
            System.out.println(". 性别:" + sex + ".");
        });
    }
}
