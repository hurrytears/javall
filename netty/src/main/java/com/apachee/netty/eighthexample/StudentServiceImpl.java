package com.apachee.netty.eighthexample;
import io.grpc.stub.StreamObserver;

public class StudentServiceImpl extends com.apachee.netty.eighthexample.StudentServiceGrpc.StudentServiceImplBase {

    @Override
    public void getRealNameByUsername(com.apachee.netty.eighthexample.MyRequest request, StreamObserver<com.apachee.netty.eighthexample.MyResponse> responseObserver) {
        System.out.println("接收到客户端信息"+ request.getUsername());

        responseObserver.onNext(com.apachee.netty.eighthexample.MyResponse.newBuilder().setRealname("张三").build());
        responseObserver.onCompleted();
    }
}
