package com.apachee.netty.pratice;

import com.apachee.netty.protobuf.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<byte[]> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        if(msg.length > 0){
            DataInfo.Student student = DataInfo.Student.parseFrom(msg);
            System.out.println(student.getName());
            System.out.println(student.getAge());
            System.out.println(student.getAddress());
        }
        DataInfo.Student student2 = DataInfo.Student.newBuilder().setName("小李").setAge(14).setAddress("东方红广场").build();
        ctx.channel().writeAndFlush(student2.toByteArray());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("来自客户端的问候");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端出bug了");
    }
}
