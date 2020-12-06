package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WorldCountCluster {
    public static void main(String[] args) {
        //放到集群执行
        //--master yarn 执行不通过，
        //脚本执行无日志
        SparkConf conf = new SparkConf().setAppName("CountingSheep");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("hdfs://centos:8020/test/spark/data/wordfile");
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> pairs = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> count = pairs.reduceByKey((v1, v2) -> v1 + v2);

        count.foreach(value -> System.out.println(value._1 +":"+ value._2));
    }

}
