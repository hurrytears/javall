package com.apachee.stream;

import java.util.*;
import java.util.stream.Stream;

public class Demo01Stream {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("张无忌");
        list.add("周芷若");
        list.add("赵敏");
        list.add("张强");
        list.add("张三丰");

        list.stream()
                .filter(name -> name.startsWith("张"))
                .filter(name -> name.length() == 3)
                .forEach(System.out::println);
    }

    /*
    两种获取stream的方式
    Collection的stream()方法
    static <T> Stream<T> of (T... values)
     */
    static void getStream(){
        List<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();

        Set<String> set = new HashSet<>();
        Stream<String> stream2 = set.stream();

        Map<String, String> map = new HashMap<>();
        Set<String> keySet = map.keySet();
        Stream<String> stream3 = keySet.stream();

        Collection<String> values = map.values();
        Stream<String> stream4 = values.stream();

        Set<Map.Entry<String,String>> entries = map.entrySet();
        Stream<Map.Entry<String,String>> stream5 = entries.stream();

        Integer[] arr = {1,2,3,4,5};
        Stream<Integer> stream6 = Stream.of(arr);
        String[] arr2 = {"a", "b", "c"};
        Stream<String> stream7 = Stream.of(arr2);
    }

    /*
    延迟方法: filter(Predicate pre)
    map
    limit
    skip
    concat
     */


    /*
    终结方法，count, foreach(Consumer con)*/
}
