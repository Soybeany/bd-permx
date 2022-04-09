package com.soybeany.permx.core.filter;

import com.soybeany.permx.api.IAuthExceptionProcessor;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ShiroFilterManager implements InitializingBean {

    @Autowired
    private DefaultWebSecurityManager securityManager;
    @Autowired
    private ShiroFilterFactoryBeanImpl shiroFilterFactoryBean;
    @Autowired
    private IAuthExceptionProcessor authExceptionProcessor;
    @Autowired
    private AbstractShiroFilter shiroFilter;

    @Override
    public void afterPropertiesSet() {
        // 切换session模式
        securityManager.setSessionMode(DefaultWebSecurityManager.NATIVE_SESSION_MODE);
        // 定义拦截规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMap());
        // 定义拦截器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new ShiroFormAuthenticationFilter(authExceptionProcessor));
        filters.put("perms", new ShiroPermissionsAuthorizationFilter(authExceptionProcessor));
        // 重新创建调用链
        FilterChainManager manager = shiroFilterFactoryBean.createNewFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        shiroFilter.setFilterChainResolver(chainResolver);
    }

    // ***********************内部方法****************************

    private Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> result = new LinkedHashMap<>();
        for (CheckRule rule : CheckRuleStorage.getAllRules()) {
            String definition;
            if (rule instanceof CheckRule.WithAnonymity) {
                definition = "anon";
            } else {
                CheckRule.WithPermission pRule = (CheckRule.WithPermission) rule;
                definition = "authc";
                Set<String> permissions = pRule.getRequiredPermissions();
                if (!permissions.isEmpty()) {
                    definition = definition + ", perms" + permissions;
                }
            }
            result.put(rule.getPattern(), definition);
        }
        // 补充默认安全规则
        result.put("/**", "authc");
        return result;
    }

}
