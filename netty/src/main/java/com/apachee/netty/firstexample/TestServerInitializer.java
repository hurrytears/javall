package com.apachee.netty.firstexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // netty给的handler, 用于编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 自定义hadler
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());
    }
}
