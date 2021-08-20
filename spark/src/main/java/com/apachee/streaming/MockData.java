package com.apachee.streaming;

import breeze.stats.distributions.Rand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MockData {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(9999);
        while (true) {
            Socket accept = socket.accept();
            System.out.println("接收到一个socket");
            OutputStream os = accept.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            String[] words = {"hello", "world", "you", "me"};
            Random random = new Random();
            for(int i=0; i<1000*1000; i++){
                Thread.sleep(1000);
                System.out.println("写入"+i+"条数据");
                pw.println(words[random.nextInt(words.length-1)]);
            }
            os.close();
            pw.close();
        }
    }
}
