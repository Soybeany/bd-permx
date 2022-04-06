package com.soybeany.permx.core.adapter;

import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import org.apache.shiro.config.Ini;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 将代码/yml中定义的规则转换为ini配置
 *
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class ShiroFilterAdapter extends AbstractShiroFilter {

    public ShiroFilterAdapter() {
        setStaticSecurityManagerEnabled(true);
    }

    @Override
    public void init() {
        IniFilterChainResolverFactory factory = new IniFilterChainResolverFactory(getPermDefinesIni());
        factory.setFilterConfig(getFilterConfig());
        setFilterChainResolver(factory.getInstance());
    }

    @Override
    protected WebSecurityManager createDefaultSecurityManager() {
        return new SecurityManagerAdapter(new RealmAdapter());
    }

    // ***********************内部方法****************************

    private Ini getPermDefinesIni() {
        Ini ini = new Ini();
        Ini.Section section = ini.addSection(IniFilterChainResolverFactory.URLS);
        for (CheckRule rule : CheckRuleStorage.getAllRules()) {
            String value;
            if (rule instanceof CheckRule.WithAnonymity) {
                value = DefaultFilter.anon.name();
            } else {
                Set<String> permissions = ((CheckRule.WithPermission) rule).getRequiredPermissions();
                value = DefaultFilter.authc.name() + ", perms" + permissions;
            }
            section.put(rule.getPattern(), value);
        }
        // 补一个全局认证的配置
        section.put("/**", DefaultFilter.authc.name());
        return ini;
    }

}
