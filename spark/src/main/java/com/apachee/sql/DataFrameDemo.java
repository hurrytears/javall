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
        sc.setLogLevel("ERROR");
        SQLContext sqlContext = new SQLContext(sc);
        List<String> list = Arrays.asList("zhangsan 男 18", "lisi 女 19");
        JavaRDD<People> rdd = sc.parallelize(list).map(line -> {
            String[] arr = line.split(" ");
            return new People(arr[0], arr[1], Integer.parseInt(arr[2]));
        });
        Dataset<Row> dataFrame = sqlContext.createDataFrame(rdd, People.class);
        dataFrame.show();
        dataFrame.printSchema();
        dataFrame.select("name").show();
        dataFrame.select(dataFrame.col("name"), dataFrame.col("age").plus(1)).show();
        dataFrame.filter(dataFrame.col("age").gt(18)).show();
        dataFrame.groupBy(dataFrame.col("age")).count().show();


    }
}
