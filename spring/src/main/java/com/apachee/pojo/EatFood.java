package com.apachee.pojo;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eat")
@PropertySource("classpath:eat.properties")
public class EatFood {

    //需要单独添加依赖支持
    private String eatName;
    private int eatprice;

    public String getEatName() {
        return eatName;
    }

    public void setEatName(String eatName) {
        this.eatName = eatName;
    }

    public int getEatprice() {
        return eatprice;
    }

    public void setEatprice(int eatprice) {
        this.eatprice = eatprice;
    }
}
