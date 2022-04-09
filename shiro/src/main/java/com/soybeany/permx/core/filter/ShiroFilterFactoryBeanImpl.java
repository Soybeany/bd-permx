package com.soybeany.permx.core.filter;

import com.soybeany.permx.core.api.FilterChainManagerCreator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
public class ShiroFilterFactoryBeanImpl extends ShiroFilterFactoryBean implements FilterChainManagerCreator {

    @Override
    public FilterChainManager createNewFilterChainManager() {
        return super.createFilterChainManager();
    }

}
