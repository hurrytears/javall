package com.apachee.scala.core

import org.apache.spark.{SparkConf, SparkContext}

object ActionOperation {

    def main(args: Array[String]): Unit = {
//        reduce()
//        collect()
//        count()
//        take()
//        countByKey()
//        map()
    }

    def map(): Unit ={
        val conf = new SparkConf().setMaster("local").setAppName("map")
        val sc = new SparkContext(conf)
        val numArray = Array(1,2,3,4,5,6,7,8,9,10)
        val numbers = sc.parallelize(numArray)
        val nums = numbers.map(_ * 2)
        nums.foreach(println(_))
    }

    def reduce(): Unit ={
        val conf = new SparkConf().setAppName("reduce").setMaster("local")
        val sc = new SparkContext(conf)
        val numberArray = Array(1,2,3,4,5,6,7,8,9,10)
        val numbers = sc.parallelize(numberArray)
        val sum = numbers.reduce(_ + _)
        println(sum)
    }

    def collect(): Unit ={
        val conf = new SparkConf().setAppName("reduce").setMaster("local")
        val sc = new SparkContext(conf)
        val numberArray = Array(1,2,3,4,5,6,7,8,9,10)
        val numbers = sc.parallelize(numberArray)
        val sum = numbers.map(_ * 2).collect()
        sum.foreach(println)
    }

    def count(): Unit ={
        val conf = new SparkConf().setAppName("reduce").setMaster("local")
        val sc = new SparkContext(conf)
        val numberArray = Array(1,2,3,4,5,6,7,8,9,10)
        val numbers = sc.parallelize(numberArray)
        println(numbers.count())
    }

    def take(): Unit ={
        val conf = new SparkConf().setAppName("reduce").setMaster("local")
        val sc = new SparkContext(conf)
        val numberArray = Array(1,2,3,4,5,6,7,8,9,10)
        val numbers = sc.parallelize(numberArray)
        val sum = numbers.take(3)
        sum.foreach(println)
    }

    def countByKey(): Unit ={
        val conf = new SparkConf().setAppName("reduce").setMaster("local")
        val sc = new SparkContext(conf)
        val numberArray = Array(("class1","jack"),("class1", "mary"),("class2","leo"),("class3", "lucy"))

        val numbers = sc.parallelize(numberArray).countByKey()
        for((key,value) <- numbers){
            println(key + "班级里有" + value + "名学生")
        }
    }
}
