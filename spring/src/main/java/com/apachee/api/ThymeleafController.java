package com.apachee.api;

import com.apachee.pojo.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafController {

    @RequestMapping("/thymeleaf")
    public String test(Model model){
        model.addAttribute("msg", "他们到底姓蒋还是姓汪，" +
                "我待要旁敲侧击将她访，我必须察言观色把他防");
        Msg msg = new Msg();
//        msg.setContent("Msg Content");
//        msg.setExtraInfo("Msg extrainfo");
//        msg.setTitle("Mst Title");
        model.addAttribute("msgObject", msg);
        return "thymeleaf";
    }
}
