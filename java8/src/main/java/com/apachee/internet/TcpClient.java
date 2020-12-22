package com.apachee.internet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//使用Socket
public class TcpClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8899);
        OutputStream os = socket.getOutputStream();
        os.write("你好服务器".getBytes());
        os.flush();
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        System.out.println(new String(bytes, 0, len));
        socket.close();
    }

}
