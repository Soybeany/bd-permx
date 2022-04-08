package com.soybeany.permx.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
public interface IAuthExceptionProcessor {

    /**
     * 可进行统一处理
     */
    default void onPreHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    /**
     * 认证失败(一般接口)
     */
    void onAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 鉴权失败(一般接口)
     */
    void onAuthorizationException(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 其它的异常
     */
    void onOtherAuthException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException;

}
