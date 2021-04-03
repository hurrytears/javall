package com.example

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaProducer, FlinkKafkaProducer011}

object SinkTo {

    def main(args: Array[String]): Unit = {
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

        val text = env.socketTextStream("centos", 9999)

        text.print()
        text.writeAsText("")
        text.writeAsCsv("path")


        //写入Kafka
        text.addSink(new FlinkKafkaProducer[String]("localhost:9092", "my-topic", new SimpleStringSchema()))
    }
}