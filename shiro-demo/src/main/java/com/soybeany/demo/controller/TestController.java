package com.soybeany.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @RequiresGuest
    @PostMapping("/login2")
    public String login(String account, String pwd) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(account, pwd));
        return "success";
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }

}
