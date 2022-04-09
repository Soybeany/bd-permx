package com.soybeany.permx.core.config;

import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.core.adapter.RealmAdapter;
import com.soybeany.permx.core.adapter.SessionManagerAdapter;
import com.soybeany.permx.core.api.InputAccessor;
import com.soybeany.permx.core.auth.AuthManagerImpl;
import com.soybeany.permx.core.auth.InputAccessorImpl;
import com.soybeany.permx.core.filter.ListenerFilter;
import com.soybeany.permx.core.filter.ShiroFilterManager;
import com.soybeany.permx.core.perm.ShiroAnnoPermHandler;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * todo sessionDAO、Cookie、全局拦截器未解决
 *
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
public class ShiroAutoConfiguration {

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
    Realm realm() {
        return new RealmAdapter<>();
    }

//    @ConditionalOnMissingBean
//    @Bean
//    SessionFactory sessionFactory() {
//        return new SessionFactoryAdapter<>();
//    }

//    @ConditionalOnMissingBean
//    @Bean
//    SessionDAO sessionDAO() {
//        return new SessionDaoAdapter<>();
//    }

    @ConditionalOnMissingBean
    @Bean
    SessionManager sessionManager() {
        return new SessionManagerAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    InputAccessor<?> inputAccessor() {
        return new InputAccessorImpl<>();
    }

    @Bean
    ListenerFilter<?> listenerInterceptor() {
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
     * 自定义ShiroFilter
     */
    @ConditionalOnMissingBean
    @Bean
    ShiroFilterManager shiroFilterManager() {
        return new ShiroFilterManager();
    }

}
