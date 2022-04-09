package com.soybeany.permx.core.filter;

import com.soybeany.permx.api.IAuthExceptionProcessor;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
public class ShiroPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    private final IAuthExceptionProcessor authExceptionProcessor;

    public ShiroPermissionsAuthorizationFilter(IAuthExceptionProcessor authExceptionProcessor) {
        this.authExceptionProcessor = authExceptionProcessor;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        authExceptionProcessor.onAuthorizationException((HttpServletRequest) request, (HttpServletResponse) response);
        return false;
    }

}
