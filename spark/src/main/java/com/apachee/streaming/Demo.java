package com.apachee.streaming;

import com.apachee.sql.Tuple;
import com.apachee.utils.JdbcPool;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.sql.Connection;
import java.util.*;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("streaming test")
                .setMaster("local[3]")
                .loadFromSystemProperties(true)
                .set("fs.defaultFS","hdfs://centos:8020");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        jssc.sparkContext().setLogLevel("ERROR");
        // updateStateByKey 必须开启checkPoint
        jssc.checkpoint("/test/spark/streaming_checkpoint");

        // socket 数据源
        JavaReceiverInputDStream<String> stream = jssc.socketTextStream("localhost", 9999);
        // hdfs 文件数据源
//        JavaDStream<String> stream = jssc.textFileStream("/test/spark/data");
//        JavaDStream<String> words = stream.flatMap(l -> Arrays.asList(l.split(" ")).iterator());
//        JavaPairDStream<String, Integer> pair = words.mapToPair(word -> new Tuple2<>(word, 1));
        // kafka数据源 官方最新版
//        Collection<String> topics = Arrays.asList("topicA", "topicB");
//        Map<String, Object> kafkaParams = new HashMap<>();
////        kafkaParams.put("bootstrap.servers", "localhost:9092,anotherhost:9092");
//        kafkaParams.put("bootstrap.servers", "centos:9092");
//        kafkaParams.put("key.deserializer", StringDeserializer.class);
//        kafkaParams.put("value.deserializer", StringDeserializer.class);
//        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
//        kafkaParams.put("auto.offset.reset", "latest");
//        kafkaParams.put("enable.auto.commit", false);
//        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
//                jssc,
//                LocationStrategies.PreferConsistent(),
//                ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
//        );
//        JavaPairDStream<String, String> kafkaPair = stream.mapToPair(record -> new Tuple2<>(record.key(), record.value()));
//        JavaPairDStream<String, Integer> pair = kafkaPair.mapToPair(v -> new Tuple2<>(v._2, 1));
//        JavaPairDStream<String, Integer> wordcount = pair.reduceByKey((v1, v2) -> v1 + v2);
        // updateSteteByKey 测试
//        JavaPairDStream<String, Integer> wordcount = pair.updateStateByKey((List<Integer> values, Optional<Integer> state) -> {
//            int newValue = state.isPresent() ? state.get() : 0;
//            for (Integer value : values) {
//                newValue += value;
//            }
//            return Optional.of(newValue);
//        });

        // 黑名单
//        List<Tuple2<String, Boolean>> blackListData = new ArrayList<>();
//        blackListData.add(new Tuple2<>("jack", false));
//        blackListData.add(new Tuple2<>("mary", false));
//        JavaPairRDD<String, Boolean> blackListRDD = jssc.sparkContext().parallelizePairs(blackListData);
//        JavaPairDStream<String, String> clickLogDStream = stream.mapToPair(line -> new Tuple2<>(line.split(" ")[1], line));
//        JavaDStream<Tuple2<String, String>> validLog = clickLogDStream.transform(clickRDD -> {
//            JavaRDD<Tuple2<String, String>> filtered = clickRDD.leftOuterJoin(blackListRDD)
//                    .filter(log -> log._2._2.orElse(true))
//                    .map(f -> new Tuple2<String, String>(f._1, f._2._1.split(" ")[0]));
//            return filtered;
//        });
//        validLog.print();

        // 滑动窗口
        JavaPairDStream<String, Integer> top = stream.mapToPair(l -> new Tuple2<>(l.split(" ")[1], 1))
                .reduceByKeyAndWindow((v1, v2) -> v1 + v2, Durations.seconds(60), Durations.seconds(10))
                .transformToPair(rdd -> rdd.mapToPair(v -> new Tuple2<Integer, String>(v._2, v._1))
                        .sortByKey(false)
                        .mapToPair(v -> new Tuple2<String, Integer>(v._2, v._1)));
//        top.print();
        // foreachRDD 使用方法
        top.foreachRDD(rdd -> {
            rdd.foreachPartition(partition -> {
                Connection conn = JdbcPool.getConnection();
                while (partition.hasNext()){
                    Tuple2<String, Integer> next = partition.next();
                    String sql = "insert into spark.wordcount (word,count) values" +
                            "('"+next._1+"',"+next._2+")";
                    conn.createStatement().execute(sql);
                }
                JdbcPool.returnConnection(conn);
            });
        });


        jssc.start();
        jssc.awaitTermination();

    }
}
