package com.soybeany.permx.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 */
@Component
@ConfigurationProperties(prefix = "permx-shiro")
public class PermxShiroConfig {

    /**
     * 是否允许自动更新会话
     */
    private boolean enableUpdateSession = true;

    public boolean getEnableUpdateSession() {
        return enableUpdateSession;
    }

    public void setEnableUpdateSession(boolean enableUpdateSession) {
        this.enableUpdateSession = enableUpdateSession;
    }

}
