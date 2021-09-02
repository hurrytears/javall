package com.apachee.netty.eighthexample;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class StudentServiceImpl extends com.apachee.netty.eighthexample.StudentServiceGrpc.StudentServiceImplBase {

    @Override
    public void getRealNameByUsername(com.apachee.netty.eighthexample.MyRequest request, StreamObserver<com.apachee.netty.eighthexample.MyResponse> responseObserver) {
        System.out.println("接收到客户端信息"+ request.getUsername());

        responseObserver.onNext(com.apachee.netty.eighthexample.MyResponse.newBuilder().setRealname("张三").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentsByAge(com.apachee.netty.eighthexample.StudentRequest request, StreamObserver<com.apachee.netty.eighthexample.StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息; " + request.getAge());

        responseObserver.onNext(com.apachee.netty.eighthexample.StudentResponse.newBuilder().setName("张三").setAge(20).setCity("北京").build());
        responseObserver.onNext(com.apachee.netty.eighthexample.StudentResponse.newBuilder().setName("李四").setAge(21).setCity("上海").build());
        responseObserver.onNext(com.apachee.netty.eighthexample.StudentResponse.newBuilder().setName("王麻子").setAge(21).setCity("南京").build());

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<com.apachee.netty.eighthexample.StudentRequest> getStudentsWrapperByAges(StreamObserver<com.apachee.netty.eighthexample.StudentResponseList> responseObserver) {
        return new StreamObserver<com.apachee.netty.eighthexample.StudentRequest>() {
            @Override
            public void onNext(com.apachee.netty.eighthexample.StudentRequest studentRequest) {
                System.out.println("onNext" + studentRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                com.apachee.netty.eighthexample.StudentResponse studentResponse = com.apachee.netty.eighthexample.StudentResponse.newBuilder().setName("张三").setAge(30).setCity("北京").build();
                com.apachee.netty.eighthexample.StudentResponse studentResponse2 = com.apachee.netty.eighthexample.StudentResponse.newBuilder().setName("李四").setAge(30).setCity("成都").build();

                com.apachee.netty.eighthexample.StudentResponseList build = com.apachee.netty.eighthexample.StudentResponseList.newBuilder().addStudentResponse(studentResponse).addStudentResponse(studentResponse2).build();

                responseObserver.onNext(build);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest streamRequest) {
                System.out.println(streamRequest.getRequestIno());

                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
