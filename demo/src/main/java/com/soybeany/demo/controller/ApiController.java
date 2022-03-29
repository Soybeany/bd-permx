package com.soybeany.demo.controller;

import com.soybeany.demo.impl.PermConstants;
import com.soybeany.demo.model.Input;
import com.soybeany.permx.annotation.RequireAnonymity;
import com.soybeany.permx.annotation.RequirePermissions;
import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IAuthManager<Input> authManager;
    @Autowired
    private IAuthVerifier<Input> authVerifier;

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Input input) {
        try {
            authManager.login(request, response, input, authVerifier);
            return "登录成功";
        } catch (BdPermxAuthException e) {
            return "登录失败:" + e.getMessage();
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            authManager.logout(request, response);
            return "登出成功";
        } catch (BdPermxNoSessionException e) {
            return "登出失败:" + e.getMessage();
        }
    }

    @RequireAnonymity
    @GetMapping("/testAnonymity")
    public String testAn() {
        return "ok";
    }

    @GetMapping("/testLogin")
    public String testLogin() {
        return "ok";
    }

    @RequirePermissions(PermConstants.API_COMMON)
    @GetMapping("/testPermission")
    public String testPermission() {
        return "ok";
    }

}
