package com.example

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object WordCount {

    def main(args: Array[String]): Unit = {
        // 获取执行器的环境
        //获取执行器env
        val port = 9999

        val env = StreamExecutionEnvironment.getExecutionEnvironment

        //接受端口数据
        val text = env.socketTextStream("centos", port)

        val windowCounts = text
            .flatMap(w => w.split("\\s"))
            .map(w => WordWithCount(w, 1))
            .keyBy("word")
            .timeWindow(Time.seconds(10), Time.seconds(5))
            .sum("count")

        windowCounts.print().setParallelism(1)

        env.execute("text WordCount")
    }

    case class WordWithCount(word: String, count: Long)
}
