package com.apachee.netty.sixthexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class MyClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomInt = new Random().nextInt(3);
        MyDataInfo.MyMessage message;
        if (0 == randomInt) {
            message = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.PersonType)
                    .setPerson(MyDataInfo.Person.newBuilder().setName("张三")
                            .setAge(78)
                            .setAddress("北京").build())
                    .build();
        } else if (1 == randomInt) {
            message = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.DogType)
                    .setDog(MyDataInfo.Dog.newBuilder().setName("狗").setAge(4).build())
                    .build();
        } else {
            message = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.CatType)
                    .setCat(MyDataInfo.Cat.newBuilder().setName("猫").setAge(3).build())
                    .build();
        }
        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端出bug了");
    }

}
