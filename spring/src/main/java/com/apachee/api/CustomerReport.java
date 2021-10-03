package com.apachee.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("customer")
public class CustomerReport {
    private static final Logger log = LoggerFactory.getLogger(CustomerReport.class);

    @RequestMapping("welcome")
    public ModelAndView welcome(){
        log.info("登录成功");
        return new ModelAndView("welcome");
    }

    @RequestMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        System.out.println("................");
        if(file.isEmpty()){
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = "D:\\Code\\InteliJ\\javall\\data\\spring\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            log.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return "上传失败";
    }
}
