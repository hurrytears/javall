package com.apachee.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ServletContext
 *
 */
@WebServlet("/demo10")
public class Demo10 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        ServletContext servletContext1 = this.getServletContext();
        System.out.println(servletContext == servletContext1);

        // 获取mime类型，如text/html, img/jpeg
        String fileName = "a.jpg";
        String mimeType = servletContext.getMimeType(fileName);
        System.out.println(mimeType);

        // request 的域对象，一次请求域的变量赋值练习
        //req.setAttribute("a", 1);

        servletContext.setAttribute("s", 1);
        servletContext.removeAttribute("s");
        servletContext.getAttribute("s");


        // 获取文件真实路径
        servletContext.getRealPath("demo.file");
        // web目录下, web-info目录下, src目录下/web-info/classes/xxx.txt
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
