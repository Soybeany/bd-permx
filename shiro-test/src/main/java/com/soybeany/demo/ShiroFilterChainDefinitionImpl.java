package com.soybeany.demo;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Component("shiroFilterChainDefinition")
public class ShiroFilterChainDefinitionImpl extends DefaultShiroFilterChainDefinition implements BeanPostProcessor {

    public ShiroFilterChainDefinitionImpl() {
        addPathDefinition("/api/login", "anon");
        addPathDefinition("/api/perm", "authc, perms[document:read]");
        addPathDefinition("/**", "authc");
    }

}
