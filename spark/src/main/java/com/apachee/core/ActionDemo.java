package com.apachee.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class ActionDemo {

    private static SparkConf conf = new SparkConf().setAppName("ActionDemo").setMaster("local");
    private static JavaSparkContext sc = new JavaSparkContext(conf);

    public static void main(String[] args) {

    }
}
