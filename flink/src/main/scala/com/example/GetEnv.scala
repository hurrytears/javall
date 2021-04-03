package com.example

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object GetEnv {

    def main(args: Array[String]): Unit = {

        //流处理运行时
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

        //批处理运行时
        val env2: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment


        //根据上下文环境自行创建remote 或者 local


    }

}
