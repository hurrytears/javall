package com.apachee.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 5555));

        while (true){
            selector.select();
            for(SelectionKey selectionKey: selector.selectedKeys()){
                if(selectionKey.isConnectable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    // 是否进行状态
                    if(client.isConnectionPending()){
                        // 需要客户端主动发
                        client.finishConnect();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((LocalDateTime.now()+ "连接成功").getBytes());
                        writeBuffer.flip();
                        // 写一次就直接触发事件
                        client.write(writeBuffer);

                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                        executorService.submit(() -> {
                            while (true){
                                writeBuffer.clear();
                                InputStreamReader input = new InputStreamReader(System.in);
                                BufferedReader reader = new BufferedReader(input);
                                String sendMessage = reader.readLine();
                                writeBuffer.put(sendMessage.getBytes());
                                writeBuffer.flip();
                                client.write(writeBuffer);
                            }
                        });
                    }

                    client.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int read = client.read(readBuffer);
                    System.out.println(new String(readBuffer.array(), 0, read));
                }
            }
            selector.selectedKeys().clear();
        }
    }
}
