package com.soybeany.permx.core.filter;

import com.soybeany.permx.api.IAuthExceptionProcessor;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    private final IAuthExceptionProcessor authExceptionProcessor;

    public ShiroFormAuthenticationFilter(IAuthExceptionProcessor authExceptionProcessor) {
        this.authExceptionProcessor = authExceptionProcessor;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        authExceptionProcessor.onAuthenticationException((HttpServletRequest) request, (HttpServletResponse) response);
        return false;
    }

}
