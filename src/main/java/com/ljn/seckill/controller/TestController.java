package com.ljn.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "ljn");
        return "hello";
    }
}
