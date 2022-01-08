package com.apachee.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
public class RddProgrammingGuide {

    private static SparkConf conf = new SparkConf().setAppName(RddProgrammingGuide.class.getName()).setMaster("local");
    private static JavaSparkContext sc = new JavaSparkContext(conf);

    @Test
    public void parallelizedCollections(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data, 5);
        distData.reduce(Integer::sum);
        distData.persist(StorageLevel.MEMORY_ONLY());

        // 本地文件必须出现在所有的workder节点上
        JavaRDD<String> distFile = sc.textFile("data.txt");
        /**
         * sc.textFile("/my/directory")
         * sc.textFile("/my/directory/*.txt")
         * sc.textFile("/my/directory/*.gz") 只能是gzip压缩文件
         *
         * sc.wholeTextFiles() 返回目录下的(filename, content) pairs
         * sc.sequenceFile()
         *
         * sc.hadoopRDD()
         * sc.newAPIHadoopRDD()
         *
         * rdd.saveAsObjectFile() 存储为文件
         * sc.objectFile()
         */
        distFile.map(String::length).reduce(Integer::sum);
    }

    // Understanding closures
    // Transformations
    // Actions

    // Shuffle operations
    // Performance Impact
    // persist() and  unpersist()

    @Test
    public void broadCastVariable(){
        Broadcast<Integer> integerBroadcast = sc.broadcast(1);
        System.out.println(integerBroadcast.value());
        integerBroadcast.unpersist();
        LongAccumulator accum = sc.sc().longAccumulator();

        sc.parallelize(Arrays.asList(1,2,3,4,5)).foreach(accum::add);

        System.out.println(accum.value());
    }


}
