package com.apachee.netty.seventhexample;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class ThriftServer {

    public static void main(String[] args) throws Exception {
        //非阻塞的
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        //不是高可用，是半同步半异步
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        //处理器
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        // 压缩的二进制流,这个是数据格式
        arg.protocolFactory(new TCompactProtocol.Factory());
        // frame，这个是传输方式
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        TServer server = new THsHaServer(arg);
        System.out.println("Thrift Server Started!");
        //异步非阻塞的死循环
        server.serve();
    }
}
