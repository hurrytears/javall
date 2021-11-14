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
        System.out.println("获取到请求");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from sys_user");
        for(Map<String, Object> map: maps){
            System.out.println("查询到结果");
            for (String key: map.keySet()){
                System.out.println(key + ":" + map.get(key));
            }
        }
        return Msg.success(maps);
    }
}
