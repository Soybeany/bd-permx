package com.soybeany.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @GetMapping("/perm")
    public String perm() {
        return "ok";
    }

    @PostMapping("/login")
    public String login() {
        SecurityUtils.getSubject().login(new UsernamePasswordToken("123", "abc"));
        return "ok";
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "ok";
    }

}
