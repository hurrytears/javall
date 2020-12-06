package com.apachee.java.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.math.Ordered;

import java.io.Serializable;

class SecondarySortKey implements Ordered<SecondarySortKey>, Serializable {

    private int first;
    private int second;

    public SecondarySortKey(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int compare(SecondarySortKey that) {
        if(this.first == that.first){
            return this.second - that.second;
        }else{
            return this.first - that.first;
        }
    }
}

public class SecondarySort {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("二次排序").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("data/words/second");
        JavaPairRDD<SecondarySortKey, String> pairs = lines.mapToPair(new PairFunction<String, SecondarySortKey, String>() {
            @Override
            public Tuple2<SecondarySortKey, String> call(String s) throws Exception {
                String[] arr = s.split(" ");
                SecondarySortKey key = new SecondarySortKey(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
                return new Tuple2<>(key, s);
            }
        });
        pairs.sortByKey().foreach(v -> System.out.println(v._1.getFirst() + ": " + v._1.getSecond()));

        sc.close();
    }
}
