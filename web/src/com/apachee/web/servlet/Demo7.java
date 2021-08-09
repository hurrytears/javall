package com.apachee.web.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/demo7")
public class Demo7 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 页面省略，获取post的请求体
        BufferedReader br = request.getReader();
        String line = null;
        while ((line = br.readLine()) != null){
            System.out.println(line);
        }

        this.doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 通用的获取请求参数
        // request.getParam
        // 中文乱码
        // get方式不会乱码
        // post乱码,request.setCharset
        // 请求转发，浏览器地址没有变，只能访问内部资源，转发是一次请求
        request.getRequestDispatcher("/demo5").forward(request, response);
        /*
         共享数据，在一次请求域中
            setAttribute(String name,Object value)
            getArr...
            removeAttr...
         */

        /**
         * ServletContext获取
         */
    }
}
