package com.apachee.api;

import com.apachee.pojo.Msg;
import com.apachee.spring.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerReportController {
    private static final Logger log = LoggerFactory.getLogger(CustomerReportController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 上传文件
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("upload")
    public Msg upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            return Msg.fail("未获取到文件");
        }
        String fileName = file.getOriginalFilename();
        String filePath = "D:\\javall\\data\\spring\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            return Msg.success("文件上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return Msg.fail("文件上传失败");
    }

    /**
     * 获取任务清单
     * @return
     */
    @RequestMapping("tasklist")
    public Msg taskList(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from tasks");
        return Msg.success(maps);
    }

    @RequestMapping("tasksubmit")
    public Msg taskSubmit(){

        return Msg.success();
    }
}
