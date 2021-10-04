package com.apachee.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.apachee"})
// 官方文档有问题，这两个注解必须配置
@EnableJpaRepositories(basePackages = {"com.apachee.mysql"})
@EntityScan(basePackages = {"com.apachee.mysql"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
