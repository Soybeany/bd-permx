package com.soybeany.permx.core.config;

import com.soybeany.permx.api.ICodePermHandler;
import com.soybeany.permx.core.perm.AuthAnnoPermHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
class AuthAnnoAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    ICodePermHandler authAnnoPermHandler() {
        return new AuthAnnoPermHandler();
    }

}
