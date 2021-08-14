package com.apachee.sql;

import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.expressions.Aggregator;
import org.apache.spark.sql.types.DataTypes;

import org.apache.spark.sql.functions.*;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

        Dataset<Long> id = spark.range(1, 10);


        // udaf
        Aggregator<Long, Tuple<Long, Integer>, Double> aggregator = new Aggregator<Long, Tuple<Long, Integer>, Double>() {
            @Override
            public Tuple<Long, Integer> zero() {
                return new Tuple<>(1L, 0);
            }

            @Override
            public Tuple<Long, Integer> reduce(Tuple<Long, Integer> b, Long a) {
                return new Tuple<>(a * b.getKey(), b.getValue()+1);
            }

            @Override
            public Tuple<Long, Integer> merge(Tuple<Long, Integer> b1, Tuple<Long, Integer> b2) {
                return new Tuple<>(b1.getKey()*b2.getKey(), b1.getValue()+b2.getValue());
            }

            @Override
            public Double finish(Tuple<Long, Integer> reduction) {
                return Math.pow(reduction.getKey().doubleValue(), 1/reduction.getValue().doubleValue());
            }

            @Override
            public Encoder<Tuple<Long, Integer>> bufferEncoder() {
                return null;
            }

            @Override
            public Encoder<Double> outputEncoder() {
                return null;
            }
        };

    }
}
