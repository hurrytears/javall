package com.apachee.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerReport {

    @RequestMapping(value="login")
    public String hello(){
        return "Hello World.";
    }
}
