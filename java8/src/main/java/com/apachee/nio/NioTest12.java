package com.apachee.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioTest12 {
    public static void main(String[] args) throws IOException {
        int[] ports = {5000, 5001, 5002, 5003, 5004};

        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; ++i) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);

            // selectionKey 代表一个channel和select的连接，在连接建立的时候，也就是注册的时候生成，在任意一方取消的时候消失
            // 当前状态下channel可以接受的状态只能是accept
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("监听端口： " + ports[i] + ", 状态：" + selectionKey.isAcceptable());
        }

        while (true){
            // 这个方法会阻塞，直到监听到事件
            System.out.println("检测到的事件数量: " + selector.select());

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("获得客户端连接" + socketChannel);
                } else if (selectionKey.isReadable()) {// 这个是抢票的操作--狂差12306
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    int bytesRead = 0;
                    while (true){
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(buffer);
                        if(read<=0) break;
                        buffer.flip();
                        socketChannel.write(buffer);
                        bytesRead += read;
                    }

                    System.out.println("读取 " + bytesRead + ", 来自于 "+ socketChannel);
                }
            }
        }
    }
}
