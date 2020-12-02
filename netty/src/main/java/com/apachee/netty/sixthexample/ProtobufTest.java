package com.apachee.netty.sixthexample;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student.Builder builder = DataInfo.Student.newBuilder();
        DataInfo.Student student = builder.setName("张三").setAge(20).setAddress("beijing").build();

        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student student2 = DataInfo.Student.parseFrom(student2ByteArray);
        System.out.println(student2.getName());
        System.out.println(student2.getAge());
        System.out.println(student2.getAddress());
    }
}
