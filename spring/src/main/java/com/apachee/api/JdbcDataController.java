package com.apachee.api;

import com.apachee.mysql.User;
import com.apachee.pojo.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jdbc")
public class JdbcDataController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/getusers")
    public Msg getUsers(){
        System.out.println("正在执行业务方法");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from sys_user");
        return Msg.success(maps);
    }
}
