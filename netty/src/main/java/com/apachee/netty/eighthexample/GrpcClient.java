package com.apachee.netty.eighthexample;

import com.apachee.netty.eighthexample.StudentServiceGrpc;
import com.apachee.netty.sixthexample.DataInfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {

    public static void main(String[] args) {
        ManagedChannel  managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                .usePlaintext().build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        com.apachee.netty.eighthexample.MyResponse myResponse = blockingStub.getRealNameByUsername(com.apachee.netty.eighthexample.MyRequest.newBuilder().setUsername("张三").build());
        System.out.println(myResponse.getRealname());
    }
}
