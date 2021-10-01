package com.apachee.internet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpBSServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8899);
        while (true) {
            Socket socket = serverSocket.accept(); //思考一下socket为什么放在这里
            new Thread(() -> {
                try {
                    InputStream is = new BufferedInputStream(socket.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = reader.readLine();
                    String path = line.split(" ")[1].substring(1);
                    FileInputStream fis = new FileInputStream(path);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    OutputStream os = socket.getOutputStream();
                    os.write("HTTP/1.1 200 OK\r\n".getBytes()); //1.0也可以
                    os.write("Content-Type:text/html\r\n".getBytes());
                    os.write("\r\n".getBytes());
                    while ((len = fis.read(bytes)) != -1) {
                        os.write(bytes, 0, len);
                    }
                    fis.close();
                    socket.close();

                } catch (Exception e) {
                    System.out.println(e);
                }

            }).start();
        }
//        serverSocket.close();
    }
}
