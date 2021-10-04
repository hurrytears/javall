package com.apachee.api;

import com.apachee.pojo.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class IndexController {
    public static final String templete = "Hello, %s!";
    public static final AtomicLong id = new AtomicLong();

    @RequestMapping("/")
    public Greeting login(@RequestParam(defaultValue = "游客") String username,
                          @RequestParam(defaultValue = "123") String password){
        return new Greeting(id.incrementAndGet(), String.format(templete, username));
    }

    @GetMapping("/login")
    public ModelAndView index(@RequestParam(value = "name", defaultValue = "World") String name){
        return new ModelAndView("login");
    }

}
