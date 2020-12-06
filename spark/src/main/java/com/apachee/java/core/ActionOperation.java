package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.sources.In;
import scala.Tuple2;

import javax.lang.model.type.IntersectionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ActionOperation {

    public static void main(String[] args) {
//        reduce();
//        collect();
//        count();
//        take();
//        saveAsTextFile();
//        countByKey();
//        map();
        join();
    }

    private static void join(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("join");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<Integer, String>> list = Arrays.asList(
                new Tuple2<>(1, "zhang"),
                new Tuple2<>(1, "zang"),
                new Tuple2<>(2, "wang"),
                new Tuple2<>(3, "li")
        );
        JavaPairRDD<Integer, String> student = sc.parallelizePairs(list);
        List<Tuple2<Integer, Integer>> list2 = Arrays.asList(
                new Tuple2<>(1, 80),
                new Tuple2<>(1, 70),
                new Tuple2<>(2, 90),
                new Tuple2<>(3, 100)
        );
        JavaPairRDD<Integer, Integer> score = sc.parallelizePairs(list2);
        student.join(score).foreach(v -> {
            System.out.println(v._1 + "\t" + v._2._1 + "\t" + v._2._2);
        });
        System.out.println("-------------------------");
        student.cogroup(score).foreach(v -> {
            System.out.println(v._1 + "\t" + v._2._1 + "\t" + v._2._2);
        });
    }

    private static void map(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("map");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numsRdd = sc.parallelize(nums);
        JavaRDD<Integer> map = numsRdd.map(x -> x * 2);
        map.foreach(num -> System.out.println(num)
        );
    }

    private static void reduce(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("reduce");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);

        Integer sum = numbers.reduce((v1, v2) -> v1 + v2);

        System.out.println(sum);

        sc.close();
    }

    private static void collect(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("collect");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);

        JavaRDD<Integer> res = numbers.map(v -> v * 2);
        List<Integer> list = res.collect();

        list.forEach(System.out::println);

        sc.close();
    }

    private static void count(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("count");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);

        long count = numbers.count();

        System.out.println(count);
        sc.close();
    }

    private static void take(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("take");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);

        List<Integer> top3 = numbers.take(3);

        top3.forEach(System.out::println);
        sc.close();
    }

    private static void saveAsTextFile(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("saveAsTextFile");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);

        numbers.saveAsTextFile("data/core/saveAsExample");
        sc.close();
    }

    private static void countByKey(){
        SparkConf conf = new SparkConf().setMaster("local").setAppName("countByKey");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Tuple2<String,String>> numberList = Arrays.asList(
                new Tuple2<String,String>("class1", "jack"),
                new Tuple2<String,String>("class1", "mary"),
                new Tuple2<String,String>("class2", "leo"),
                new Tuple2<String,String>("class2", "lucy")
        );
        JavaPairRDD<String, String> numbers = sc.parallelizePairs(numberList);

        Map<String, Long> studentCount = numbers.countByKey();
        for(String key: studentCount.keySet()){
            System.out.println(key + "班级里有" + studentCount.get(key) + "学生");
        }

        sc.close();
    }
}
