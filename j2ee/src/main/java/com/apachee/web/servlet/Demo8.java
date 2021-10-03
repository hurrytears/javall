package com.apachee.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重定向
 */

@WebServlet("/demo8")
public class Demo8 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(302);
        response.setHeader("location", "/demo7");
        response.setHeader("Content-type","text/html;charset=utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter();
        response.getOutputStream();
        // response.setRedirect()
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
