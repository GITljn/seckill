package com.ljn.seckill.controller;

import com.ljn.seckill.service.UserService;
import com.ljn.seckill.vo.LoginVO;
import com.ljn.seckill.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
// Slf4j是一个日志标准，不是具体实现，其具体实现包括logback、log4j、slf4j-sample，类似于JDBC
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    // @Valid 自动校验，校验的规则在类的属性上
    public R doLogin(@Valid LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        log.info("{}", loginVO);
        return userService.doLogin(loginVO, request, response);
    }
}
