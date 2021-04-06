package com.apachee.java.streaming;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("Simple Application").master("local").config("spark.testing.memory","471859233").getOrCreate();
        Dataset<Row> lines = spark
                .readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", 8899)
                .load();
        System.out.println(Encoders.STRING());
        Dataset<String> words = lines
                .as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());
        Dataset<Row> wordCounts = words.groupBy("value").count();
        StreamingQuery query = wordCounts.writeStream()
                .outputMode("complete")
                .format("console")
                .start();

        try{
            query.awaitTermination();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
