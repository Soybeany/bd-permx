package com.soybeany.permx.impl;

import com.soybeany.permx.api.IAuthExceptionProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
public class AuthExceptionProcessorImpl implements IAuthExceptionProcessor {

    @Override
    public void onPreHandle(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain; charset=utf-8");
    }

    @Override
    public void onAuthenticationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setupResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public void onAuthorizationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setupResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");

    }

    @Override
    public void onOtherAuthException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        setupResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    // ***********************内部方法****************************

    private void setupResponse(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        response.getWriter().print(msg);
    }

}
