package com.example

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object AddSources {

    def main(args: Array[String]): Unit = {

        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

        //1.通过kafka创建 DataStream
        val props = new Properties()
        props.setProperty("bootstrap.servers", "localhost:9092")
        props.setProperty("group.id", "flink-group")
        val input2: DataStream[String] = env
            .addSource(new FlinkKafkaConsumer[String]("topic", new SimpleStringSchema(), props))

        //2.通过文件创建DataStram
        val input3: DataStream[String] = env
            .readTextFile("./words")

        //3.通过fromElements创建DataStream
        case class Stock(price: Int, volume: Int){
            override def toString: String = price.toString + ": " + volume.toString
        }

        val input: DataStream[Stock] = env.fromElements(
            "100, 100",
            "120, 700"
        ).map({
            x =>
                val y: Array[String] = x.split("\\s")
                Stock(y(0).trim.toInt, y(1).trim.toInt)
        })
    }

}
