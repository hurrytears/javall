package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class MyAction {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("action");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> list = Arrays.asList(1,3,4,5,7);

        Integer reduce = sc.parallelize(list).reduce((v1, v2) -> v1 + v2);
        List<Integer> collect = sc.parallelize(list).collect();
        long count = sc.parallelize(list).count();
        List<Integer> take = sc.parallelize(list).take(2);
        sc.parallelize(list).saveAsTextFile("//");
        //countByKey
        //foreach(System.out::println);

        sc.close();
    }
}
