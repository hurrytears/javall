package com.apachee.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.Arrays;
import java.util.List;

public class DataFrameDemo {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DataFrameDemo");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        List<String> list = Arrays.asList("zhangsan 男 18", "lisi 女 19");
        JavaRDD<String> rdd = sc.parallelize(list);
        Dataset<Row> dataFrame = sqlContext.createDataFrame(rdd, People.class);
        dataFrame.show();
    }
}
