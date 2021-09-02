package com.apachee.netty.eighthexample;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;

public class GrpcClient {

    public static void main(String[] args) {
        ManagedChannel  managedChannel = ManagedChannelBuilder.forAddress("localhost", 8888)
                .usePlaintext().build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        StudentServiceGrpc.StudentServiceStub stub = StudentServiceGrpc.newStub(managedChannel);
        com.apachee.netty.eighthexample.MyResponse myResponse = blockingStub.getRealNameByUsername(com.apachee.netty.eighthexample.MyRequest.newBuilder().setUsername("张三").build());
        System.out.println(myResponse.getRealname());

        System.out.println("-------------------------");

        Iterator<com.apachee.netty.eighthexample.StudentResponse> students = blockingStub.getStudentsByAge(com.apachee.netty.eighthexample.StudentRequest.newBuilder().setAge(20).build());
        while (students.hasNext()){
            com.apachee.netty.eighthexample.StudentResponse studentResponse = students.next();
            System.out.println(studentResponse.getName()+"  "+studentResponse.getAge()+" " + studentResponse.getCity());
        }

        System.out.println("-------------------------");

        StreamObserver<com.apachee.netty.eighthexample.StudentResponseList> studentResponseListStreamObserver = new StreamObserver<com.apachee.netty.eighthexample.StudentResponseList>() {
            @Override
            public void onNext(com.apachee.netty.eighthexample.StudentResponseList studentResponseList) {
                studentResponseList.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName());
                    System.out.println(studentResponse.getAge());
                    System.out.println(studentResponse.getCity());
                    System.out.println("********");
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed!");
            }
        };

        // 流式请求，不能用blockingStub
        StreamObserver<com.apachee.netty.eighthexample.StudentRequest> studentRequestStreamObserver = stub.getStudentsWrapperByAges(studentResponseListStreamObserver);
        studentRequestStreamObserver.onNext(com.apachee.netty.eighthexample.StudentRequest.newBuilder().setAge(30).getDefaultInstanceForType());
        studentRequestStreamObserver.onNext(com.apachee.netty.eighthexample.StudentRequest.newBuilder().setAge(40).getDefaultInstanceForType());
        studentRequestStreamObserver.onNext(com.apachee.netty.eighthexample.StudentRequest.newBuilder().setAge(50).getDefaultInstanceForType());

        studentRequestStreamObserver.onCompleted();

        System.out.println("---------------------------");

        StreamObserver<StreamRequest> requestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse streamResponse) {
                System.out.println(streamResponse.getResponseInfo());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        for(int i=0; i<10; i++){
            requestStreamObserver.onNext(StreamRequest.newBuilder().setRequestIno(LocalDateTime.now().toString()).build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------");


    }
}

