package com.soybeany.demo;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
public class TestFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("Failure");
        return false;
    }

}
