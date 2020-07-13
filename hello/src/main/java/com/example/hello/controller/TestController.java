package com.example.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/jump")
    public String pageJump(){
        System.out.println("页面跳转...");
        return "index";
    }
}
