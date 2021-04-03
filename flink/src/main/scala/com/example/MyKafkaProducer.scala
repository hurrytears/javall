package com.example

import java.util.Properties

import org.apache.kafka.clients.producer._

object MyKafkaProducer {

    def main(args: Array[String]): Unit = {
        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos:9092")
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
        props.put("acks", "all")
        props.put("request.timeout.ms", "6000")
        println(props.values())
        val producer = new KafkaProducer[String, String](props)
        for (i <- 1 to 100) {
            val msg = s"${i}: this is a dog ${i} kafka data"
            println("send -->" + msg)
            // 得到返回值
            val rmd: RecordMetadata = producer.send(new ProducerRecord[String, String]("dog", msg)).get()
            println(rmd.toString)
            Thread.sleep(500)
        }

        producer.close()
    }

}
