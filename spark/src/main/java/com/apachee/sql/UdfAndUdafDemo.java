package com.apachee.sql;

import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.expressions.Aggregator;
import org.apache.spark.sql.types.DataTypes;

public class UdfAndUdafDemo {

    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSession.builder().appName("udf or udaf").master("local").getOrCreate();
        spark.udf().register("udf", new UDF1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                return integer + 1;
            }
        }, DataTypes.IntegerType);
        Dataset<Row> json = spark.read().json("data/json");
        spark.sql("select udf(1)").show();


        // udaf
        Aggregator<Long, Tuple, Double> aggregator = new Aggregator<Long, Tuple, Double>() {
            @Override
            public Tuple zero() {
                return new Tuple(1L, 0);
            }

            @Override
            public Tuple reduce(Tuple b, Long a) {
                long newKey = a + b.getKey();
                int newValue = b.getValue() + 1;
                b.setKey(newKey);
                b.setValue(newValue);
                return b;
            }

            @Override
            public Tuple merge(Tuple b1, Tuple b2) {
                long newKey = b1.getKey()+b2.getKey();
                int newValue = b1.getValue()+b2.getValue();
                b1.setKey(newKey);
                b1.setValue(newValue);
                return b1;
            }

            @Override
            public Double finish(Tuple reduction) {
                reduction.getKey();
                reduction.getValue();
                return (double)reduction.getKey() / reduction.getValue();
            }

            @Override
            public Encoder bufferEncoder() {
                System.out.println("在执行");
                return Encoders.bean(Tuple.class);
            }

            @Override
            public Encoder<Double> outputEncoder() {
                System.out.println("在执行");
                return Encoders.DOUBLE();
            }
        };
        Dataset<Long> id = spark.range(1, 5);
        id.show();
        id.registerTempTable("idtable");
        TypedColumn<Long, Double> average_salary = aggregator.toColumn().name("average_salary");
        Dataset<Double> res = id.select(average_salary);
        res.show();
    }
}
