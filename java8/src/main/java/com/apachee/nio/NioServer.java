package com.apachee.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(5555));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("阻塞监听：" + selector.select());

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

            while (iter.hasNext()) {
                final SocketChannel client;
                SelectionKey selectionKey = iter.next();
                iter.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    String key = "[" + UUID.randomUUID().toString() + "]";
                    clientMap.put(key, client);
                } else if (selectionKey.isReadable()) {
                    client = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer);
                    buffer.flip();
                    Charset charset = Charset.forName("utf-8");
                    String recievedMessage = String.valueOf(charset.decode(buffer).array());
                    System.out.println(client + ": " + recievedMessage);
                    String senderKey = null;
                    for (String k : clientMap.keySet()) {
                        if (client == clientMap.get(k)) {
                            senderKey = k;
                            break;
                        }
                    }
                    System.out.println("sender: " + senderKey);
                    for (String k : clientMap.keySet()) {
                        SocketChannel other = clientMap.get(k);
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((senderKey + ": " + recievedMessage).getBytes());
                        writeBuffer.flip();
                        other.write(writeBuffer);
                    }
                }

            }
        }
    }
}
