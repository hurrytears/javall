package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/*
wordcount：
    1.统计文本中单词出现的个数
    2.将结果按照出现次数从大到小展示前10个
 */
public class WordCount {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("localdata/spark/wordcount.txt");
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> result = pairs.reduceByKey((v1, v2) -> v1 + v2);
        JavaPairRDD<Integer, String> reversed = result.mapToPair(map -> new Tuple2<>(map._2, map._1));
        JavaPairRDD<Integer, String> sorted = reversed.sortByKey(false);
        sorted.take(10).forEach(v -> System.out.println(v._2 + ": " + v._1));

    }

}
