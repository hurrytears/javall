package com.apachee.scala.core

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Notes {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local").setAppName("广播变量")
        val sc = new SparkContext(conf)

        val list = List(1,2,3,4,5,6)

        val cached = sc.parallelize(list).persist(StorageLevel.MEMORY_ONLY_SER)
        cached.unpersist()

        val name = sc.broadcast("leo")
        val accumulator = sc.longAccumulator
        accumulator.add(10L)


        println(name.value)
        println(accumulator.value)

    }
}
