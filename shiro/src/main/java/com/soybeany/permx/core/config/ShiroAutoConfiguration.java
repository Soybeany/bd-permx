package com.soybeany.permx.core.config;

import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.core.adapter.*;
import com.soybeany.permx.core.auth.AuthManagerImpl;
import com.soybeany.permx.core.perm.ShiroAnnoPermHandler;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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
    SecurityManager securityManager() {
        return new SecurityManagerAdapter();
    }

    @ConditionalOnMissingBean
    @Bean
    Realm realm() {
        return new RealmAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    SubjectFactory subjectFactory() {
        return new SubjectFactoryAdapter();
    }

    @ConditionalOnMissingBean
    @Bean
    SessionFactory sessionFactory() {
        return new SessionFactoryAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    SessionDAO sessionDAO() {
        return new SessionDaoAdapter<>();
    }

    @ConditionalOnMissingBean
    @Bean
    ISessionManager<?, ?> sessionManager() {
        return new SessionManagerAdapter<>();
    }

}
