package com.soybeany.permx.core.filter;

import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.core.adapter.SessionDaoAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ListenerFilter<S> implements BeanPostProcessor, Filter {

    @Lazy
    @Autowired
    private IAuthListener<S> listener;

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            listener.onStartRequest((HttpServletRequest) request);
        } catch (Exception ignore) {
        }
        try {
            SessionDaoAdapter.loadSessionOptional().ifPresent(session -> listener.onFoundSession((S) session));
            chain.doFilter(request, response);
        } finally {
            listener.onFinishRequest((HttpServletRequest) request);
        }
    }
}
