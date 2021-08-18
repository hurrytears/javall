package com.apachee.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.logging.Logger;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("streaming test").setMaster("local[2]").loadFromSystemProperties(true);
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        jssc.sparkContext().setLogLevel("ERROR");

        JavaReceiverInputDStream<String> stream = jssc.socketTextStream("centos", 9999);
        JavaDStream<String> words = stream.flatMap(l -> Arrays.asList(l.split(" ")).iterator());
        JavaPairDStream<String, Integer> pair = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairDStream<String, Integer> wordCount = pair.reduceByKey((v1, v2) -> v1 + v2);

        wordCount.print();

        jssc.start();
        jssc.awaitTermination();

    }
}
