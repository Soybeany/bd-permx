package com.soybeany.permx.core.config;

import com.soybeany.permx.core.filter.ShiroFilterFactoryBeanImpl;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
public class ShiroWebFilterConfigurationImpl extends AbstractShiroWebFilterConfiguration implements BeanPostProcessor {

    @Bean
    @Override
    protected ShiroFilterFactoryBeanImpl shiroFilterFactoryBean() {
        ShiroFilterFactoryBeanImpl filterFactoryBean = new ShiroFilterFactoryBeanImpl();
        filterFactoryBean.setLoginUrl(loginUrl);
        filterFactoryBean.setSuccessUrl(successUrl);
        filterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setGlobalFilters(globalFilters());
        return filterFactoryBean;
    }

}
