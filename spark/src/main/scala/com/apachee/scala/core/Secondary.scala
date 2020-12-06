package com.apachee.scala.core

import java.io.Serializable

import org.apache.spark.{SparkConf, SparkContext}

import scala.math.Ordered

class SecondarySortKey(var first: Int, var second: Int) extends Ordered[SecondarySortKey] with Serializable {
    override def compare(that: SecondarySortKey): Int = if (this.first == that.first) this.second - that.second
    else this.first - that.first
}

object Secondary {

    def main(args: Array[String]): Unit = {
        val conf  = new SparkConf().setAppName("secondary").setMaster("local")
        val sc = new SparkContext(conf)

        val lines = sc.textFile("data/words/second")
        val pairs = lines.map(line => {
            val arr = line.split(" ")
            (new SecondarySortKey(arr(0).toInt, arr(1).toInt), line)
        })
        pairs.sortByKey().foreach(v => println(v._2))
    }
}
