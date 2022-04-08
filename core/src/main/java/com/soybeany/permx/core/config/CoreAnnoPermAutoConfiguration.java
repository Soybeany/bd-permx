package com.soybeany.permx.core.config;

import com.soybeany.permx.core.perm.PermHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
class CoreAnnoPermAutoConfiguration {

    @Bean
    PermHandler permHandler() {
        return new PermHandler();
    }

}
