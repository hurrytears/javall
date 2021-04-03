package com.example

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object MyKafkaReciever {

    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        val props = new Properties()
        props.setProperty("bootstrap.servers", "192.168.6.131:9092")
        val input: DataStream[String] = env
            .addSource(new FlinkKafkaConsumer[String]("dog", new SimpleStringSchema(), props))

        input.writeAsText("data/kafka")

    }

}
