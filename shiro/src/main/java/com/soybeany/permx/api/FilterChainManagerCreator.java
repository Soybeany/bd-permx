package com.soybeany.permx.api;

import org.apache.shiro.web.filter.mgt.FilterChainManager;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
public interface FilterChainManagerCreator {

    FilterChainManager createNewFilterChainManager();

}
