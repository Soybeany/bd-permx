package com.soybeany.demo;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Configuration
public class ShiroWebFilterConfigurationImpl extends AbstractShiroWebFilterConfiguration implements BeanPostProcessor {

    @Bean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean bean = super.shiroFilterFactoryBean();
        Map<String, Filter> filters = bean.getFilters();
        filters.put("authc", new TestFilter());
        return bean;
    }

}
