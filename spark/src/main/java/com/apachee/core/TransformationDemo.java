package com.apachee.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformationDemo {

    private static SparkConf conf = new SparkConf().setAppName("transformationDemo").setMaster("local");
    private static JavaSparkContext sc = new JavaSparkContext(conf);
    private static JavaRDD<String> lines = null;

    static {
        List<String> list = new ArrayList<>();
        list.add("apple banana");
        list.add("apple banana orange");
        list.add("apple ");
        lines = sc.parallelize(list);
        //        JavaRDD<String> lines = sc.textFile("hdfs://centos:9000/test/spark/data/wordfile");
    }

    public static void main(String[] args) {
        System.out.println("---------------map----------------");
        map();
        System.out.println("---------------filter----------------");
        filter();
        System.out.println("---------------flatMap----------------");
        flatMap();
        System.out.println("---------------groupByKey----------------");
        groupByKey();
        System.out.println("---------------reduceByKey----------------");
        reduceByKey();
        System.out.println("---------------sortByKey----------------");
        sortByKey();
        System.out.println("---------------join----------------");
        join();
        System.out.println("---------------cogroup----------------");
        cogroup();
    }

    static void map(){
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<String, Integer>(word, 1));
        JavaPairRDD<String, Integer> count = pairs.reduceByKey((v1, v2) -> v1 + v2);
        count.foreach(s -> System.out.println(s._1 + " : " + s._2));
    }
    static void filter(){
        // 这里直接做成方法引用会报错
        lines.foreach(line -> System.out.println(line));
    }
    static void flatMap(){

    }
    static void groupByKey(){

    }
    static void reduceByKey(){

    }
    static void sortByKey(){

    }
    static void join(){

    }
    static void cogroup(){

    }
}
