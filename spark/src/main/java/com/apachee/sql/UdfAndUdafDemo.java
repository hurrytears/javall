package com.apachee.sql;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import scala.collection.Seq;

public class UdfAndUdafDemo {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("udf or udaf").master("local").getOrCreate();
        spark.udf().register("udf", new UserDefinedFunction() {
            @Override
            public boolean nullable() {
                return false;
            }

            @Override
            public Column apply(Seq<Column> exprs) {
                return null;
            }

            @Override
            public Column apply(Column... exprs) {
                return null;
            }

            @Override
            public UserDefinedFunction asNondeterministic() {
                return null;
            }

            @Override
            public boolean deterministic() {
                return false;
            }

            @Override
            public UserDefinedFunction withName(String name) {
                return null;
            }

            @Override
            public UserDefinedFunction asNonNullable() {
                return null;
            }
        });
    }
}
