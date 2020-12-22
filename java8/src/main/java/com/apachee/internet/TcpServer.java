package com.apachee.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8899);
        while (true){
            Socket client = serverSocket.accept();
            InputStream inputStream = client.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            System.out.println(new String(bytes, 0, len));
            OutputStream outputStream = client.getOutputStream();
            outputStream.write("你也好啊小傻瓜".getBytes());
            client.close();
        }
    }
}
