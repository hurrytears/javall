package com.apachee.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.Buffer;

/**
 * request获取请求信息
 */

@WebServlet("/demo5")
public class Demo5 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方式
        String method = req.getMethod();
        System.out.println(method);
        // 获取虚拟目录
        String context = req.getContextPath();
        System.out.println(context);
        // 获取Servlet路径
        String servletPath = req.getServletPath();
        System.out.println(servletPath);
        // 获取get方式请求参数
        String queryString = req.getQueryString();
        System.out.println(queryString);
        // 获取请求URI:
        String uri = req.getRequestURI();
        StringBuffer url = req.getRequestURL();
        System.out.println(uri);
        System.out.println(url);
        // 获取协议版本
        String protocol = req.getProtocol();
        System.out.println(protocol);
        // 获取客户机的IP地址
        String remoteAddr = req.getRemoteAddr();
        System.out.println(remoteAddr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
