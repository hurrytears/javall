package com.apachee.netty.official.DiscardServer;

import io.grpc.netty.shaded.io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * DiscardServerHandler extends ChannelInboundHandlerAdapter,
 * which is an implementation of ChannelInboundHandler.
 * ChannelInboundHandler provides various event handler
 * methods that you can override. For now, it is just enough to
 * extend ChannelInboundHandlerAdapter rather than to implement
 * the handler interface by yourself.
 *
 * We override the channelRead() event handler method here. This
 * method is called with the received message, whenever new data
 * is received from a client. In this example, the type of the
 * received message is ByteBuf.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Discard the recieved data silently.
        ((ByteBuf) msg).release();

        // 正常情况下，ByteBuf 必须显式地释放，这个channelRead方法正确的写法是
        try {
            // Do something with msg
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
