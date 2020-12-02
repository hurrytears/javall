package com.apachee.netty.pratice;

import com.apachee.netty.protobuf.DataInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<byte[]> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        DataInfo.Student student = DataInfo.Student.parseFrom(msg);
        DataInfo.Student student2 = DataInfo.Student.newBuilder().setName("张三").setAge(18).setAddress("beijing").build();
        Channel channel = ctx.channel();
        channel.writeAndFlush(student2.toByteArray());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("上线：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("下线：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("注册：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("注销：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("don't mind, little bug");
        ctx.close();
    }
}
