package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.sources.In;
import org.codehaus.janino.Java;
import scala.Int;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MyTransformation {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("wordcount");
        JavaSparkContext sc = new JavaSparkContext(conf);

//        wordcount(sc);

//        map(sc);
//        flatMap(sc);
//        filter(sc);
//        groupByKey(sc);
//        reduceByKey(sc);
//        sortByKey(sc);
        join(sc);
        cogroup(sc);

        sc.close();
    }

    private static void cogroup(JavaSparkContext sc){

    }

    private static void join(JavaSparkContext sc){

    }

    private static void sortByKey(JavaSparkContext sc){
        List<Tuple2<Integer, Integer>> scores = Arrays.asList(
                new Tuple2<>(10, 100),
                new Tuple2<>(6, 10),
                new Tuple2<>(6, 11),
                new Tuple2<>(1, 20)
        );
        JavaPairRDD<Integer, Integer> pairs = sc.parallelizePairs(scores);
        pairs.sortByKey().foreach(v -> System.out.println(v));
    }

    private static void reduceByKey(JavaSparkContext sc){
        List<Tuple2<String, Integer>> scores = Arrays.asList(
                new Tuple2<>("class1", 50),
                new Tuple2<>("class1", 30),
                new Tuple2<>("class2", 59),
                new Tuple2<>("class2", 31)
        );
        JavaPairRDD<String, Integer> res = sc.parallelizePairs(scores).reduceByKey((v1, v2) -> v1 + v2);
        res.foreach(v -> System.out.println(v));
    }

    private static void groupByKey(JavaSparkContext sc ){
        List<Tuple2<String, Integer>> scores = Arrays.asList(new Tuple2("class1", 10), new Tuple2<>("class2", 20), new Tuple2<>("class1", 17));
        JavaPairRDD<String, Integer> pairs = sc.parallelizePairs(scores);
        JavaPairRDD<String, Iterable<Integer>> groups = pairs.groupByKey();
        groups.foreach(v -> System.out.println(v._1 + ": " + v._2.toString()));
    }

    private static void filter(JavaSparkContext sc){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7);
        JavaRDD<Integer> init = sc.parallelize(list);
        JavaRDD<Integer> filter = init.filter(v -> v % 2 == 0);
        filter.foreach(v -> System.out.println(v));
    }

    private static void flatMap(JavaSparkContext sc){
        List<String> list = Arrays.asList("hello you");
        JavaRDD<String> lines = sc.parallelize(list);
        JavaRDD<String> res = lines.flatMap(l -> Arrays.asList(l.split(" ")).iterator());
        res.foreach(s -> System.out.println(s));

    }

    private static void map(JavaSparkContext sc){
        List<Integer> list = Arrays.asList(1,2,4,4,5);
        JavaRDD<Integer> init = sc.parallelize(list);
        JavaRDD<Integer> mapped = init.map(value -> value * 2);
        mapped.foreach(v -> System.out.println(v));
    }

    private static void wordcount(JavaSparkContext sc){
        JavaRDD<String> lines = sc.textFile("data/words/wordfile");
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> map = pairs.reduceByKey((v1, v2) -> v1 + v2);
        map.foreach(value -> System.out.println("单词" + value._1 + " 出现 " + value._2 + "次"));
    }
}
