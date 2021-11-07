package com.apachee.api;

import com.apachee.pojo.EatFood;
import com.apachee.pojo.Greeting;
import com.apachee.pojo.Msg;
import com.apachee.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class IndexController {
    // 自定义属性，application.properties 注入
    @Value("${game.enterprise}")
    private String gameEnterprise;
    @Value("${game.name}")
    private String gameName;
    @Value("${game.taste}")
    private String taste;

    @Autowired
    private EatFood eatFood;

    public static final String templete = "Hello, %s!";
    public static final AtomicLong id = new AtomicLong();

    @RequestMapping("/")
    public Greeting login(@RequestParam(defaultValue = "游客") String username,
                          @RequestParam(defaultValue = "123") String password){
        DateUtil dateUtil = new DateUtil();
        System.out.println(dateUtil.getCurrentTime());
        return new Greeting(id.incrementAndGet(), String.format(templete, username));
    }

    @RequestMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("home");
    }

    @RequestMapping("/security")
    public String index(Model model){
        Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

}
