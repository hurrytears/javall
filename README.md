protobuf 生成命令：protoc --java_out=dir *.proto
thrift 生成命令：thrift --gen java protobufAndThrift/data.thrift
打包命令 mvn clean package  
执行命令 java -jar xxx.jar --server.port=8081
