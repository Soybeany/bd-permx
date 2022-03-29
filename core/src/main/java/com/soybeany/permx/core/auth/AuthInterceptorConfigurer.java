package com.soybeany.permx.core.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class AuthInterceptorConfigurer implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor<?, ?> interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**").order(-1);
    }

}