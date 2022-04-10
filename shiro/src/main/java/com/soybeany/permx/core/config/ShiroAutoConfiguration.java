package com.soybeany.permx.core.config;

import com.soybeany.permx.api.FilterChainBuilder;
import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.api.InputAccessor;
import com.soybeany.permx.core.adapter.CookieAdapter;
import com.soybeany.permx.core.adapter.RealmAdapter;
import com.soybeany.permx.core.adapter.SessionDaoAdapter;
import com.soybeany.permx.core.adapter.SessionManagerAdapter;
import com.soybeany.permx.core.auth.AuthManagerImpl;
import com.soybeany.permx.core.auth.InputAccessorImpl;
import com.soybeany.permx.core.filter.ListenerFilter;
import com.soybeany.permx.core.filter.ShiroFilterManager;
import com.soybeany.permx.core.perm.ShiroAnnoPermHandler;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.servlet.Cookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
public class ShiroAutoConfiguration implements BeanPostProcessor {

    @ConditionalOnMissingBean
    @Bean
    RememberMeManager rememberMeManager() {
        return null;
    }

    @ConditionalOnMissingBean
    @Bean
    Realm realm() {
        return new RealmAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    SessionDAO sessionDAO() {
        return new SessionDaoAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    SessionManager sessionManager(SessionDAO sessionDAO, @Qualifier("sessionCookieTemplate") Cookie cookieTemplate) {
        return new SessionManagerAdapter(sessionDAO, cookieTemplate);
    }

    @ConditionalOnMissingBean
    @Bean
    Cookie sessionCookieTemplate() {
        return new CookieAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    ICodePermHandler codePermHandler() {
        return new ShiroAnnoPermHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    IAuthManager<?> authManager() {
        return new AuthManagerImpl<>();
    }

    @ConditionalOnMissingBean
    @Bean
    InputAccessor<?> inputAccessor() {
        return new InputAccessorImpl<>();
    }

    @Bean
    ListenerFilter<?> listenerFilter() {
        return new ListenerFilter<>();
    }

    // ***********************高级自定义****************************

    /**
     * 禁用shiro原本的注解处理器
     */
    @Bean
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        AopAllianceAnnotationsAuthorizingMethodInterceptor advice = (AopAllianceAnnotationsAuthorizingMethodInterceptor) advisor.getAdvice();
        advice.setMethodInterceptors(Collections.emptyList());
        return advisor;
    }

    /**
     * 自定义filterChainBuilder
     */
    @ConditionalOnMissingBean
    @Bean
    FilterChainBuilder filterChainBuilder() {
        return new ShiroFilterManager();
    }

}
