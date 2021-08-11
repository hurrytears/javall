package com.apachee.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cookie")
public class Demo12 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie coo: cookies){
                System.out.println(coo.getName() + " : "+ coo.getValue());
            }
        }
        Cookie cookie = new Cookie("msg", "hello");
        Cookie cookie1 = new Cookie("com.esen.esenface.web.login.user", "admin");
        cookie1.setPath("/");
        cookie1.setMaxAge(0);
        cookie.setMaxAge(100);
        cookie.setDomain(".baidu.com");
        response.addCookie(cookie);
        response.addCookie(cookie1);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
