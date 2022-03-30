package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface IAuthManager<Input> {

    /**
     * 登录
     */
    @SuppressWarnings("UnusedReturnValue")
    String login(HttpServletRequest request, HttpServletResponse response, Input input, IAuthVerifier<Input> verifier) throws BdPermxAuthException;

    /**
     * 登出
     */
    void logout(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException;

}
