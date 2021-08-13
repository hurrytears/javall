package com.apachee.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
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

        dataFrame.registerTempTable("peoples");
        Dataset<Row> sql = sqlContext.sql("select * from peoples");
        JavaRDD<Row> rowJavaRDD = sql.javaRDD();

        System.out.println("--------------------------------------");
            // 动态创建

        // RowFactory.create(v1, v2, v3);
        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
        fields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("sex", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);
        Dataset<Row> df = sqlContext.createDataFrame(rowJavaRDD, schema);
        df.show();
        System.out.println("我就不信了");
    }
}
