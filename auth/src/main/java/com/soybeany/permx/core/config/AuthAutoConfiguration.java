package com.soybeany.permx.core.config;

import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.core.auth.AuthManagerImpl;
import com.soybeany.permx.core.session.SessionManagerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
class AuthAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    IAuthManager<?> authManager() {
        return new AuthManagerImpl<>();
    }

    @ConditionalOnMissingBean
    @Bean
    ISessionManager<?, ?> sessionManager() {
        return new SessionManagerImpl<>();
    }

}
