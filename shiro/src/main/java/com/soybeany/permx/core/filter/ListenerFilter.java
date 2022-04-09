package com.soybeany.permx.core.filter;

import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.core.adapter.SessionManagerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ListenerFilter<S> implements Filter {

    @Autowired
    private IAuthListener<S> listener;

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SessionManagerAdapter.loadSession().ifPresent(session -> listener.onFoundSession((S) session));
        chain.doFilter(request, response);
    }
}
