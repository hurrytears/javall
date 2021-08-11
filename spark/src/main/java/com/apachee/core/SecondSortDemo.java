package com.apachee.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Serializable;
import scala.Tuple2;
import scala.math.Ordered;

import java.util.ArrayList;
import java.util.List;

public class SecondSortDemo {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1 2");
        list.add("2 2");
        list.add("2 1");
        list.add("1 1");
        SparkConf conf = new SparkConf().setAppName("SecondSortDemo").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.parallelize(list).mapToPair(l -> {
            String[] arr = l.split(" ");
            SecondKey secondKey = new SecondKey(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
            return new Tuple2<>(secondKey, l);
        }).sortByKey().foreach(v -> System.out.println(v));
        sc.broadcast("abc");

    }
}

class SecondKey implements Ordered<SecondKey>, Serializable {
    private int first;
    private int second;

    @Override
    public String toString() {
        return "SecondKey{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public SecondKey() {
    }

    public SecondKey(int first, int second) {
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
    public int compare(SecondKey that) {
        if(this.first != that.first){
            return this.first - that.first;
        }else {
            return this.second - that.second;
        }
    }

    @Override
    public boolean $less(SecondKey that) {
        if(this.first<that.first) return true;
        else if(this.first == that.first && this.second < that.second) return true;
        else return false;
    }

    @Override
    public boolean $greater(SecondKey that) {
        if(this.first > that.first) return true;
        else if(this.first == that.first && this.second > that.second) return true;
        else return false;
    }

    @Override
    public boolean $less$eq(SecondKey that) {
        if($less(that)) return true;
        else if(this.first == that.first && this.second == that.second) return true;
        else return false;
    }

    @Override
    public boolean $greater$eq(SecondKey that) {
        if($greater(that)) return true;
        else if(this.first == that.first && this.second == that.second) return true;
        else return false;
    }

    @Override
    public int compareTo(SecondKey that) {
        if(this.first != that.first){
            return this.first - that.first;
        }else {
            return this.second - that.second;
        }
    }
}