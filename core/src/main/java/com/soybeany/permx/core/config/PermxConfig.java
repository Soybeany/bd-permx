package com.soybeany.permx.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Soybeany
 */
@Component
@ConfigurationProperties(prefix = "permx")
public class PermxConfig {

    /**
     * 定义权限
     */
    private Map<String, String> permDefine;

    /**
     * 权限url列表
     */
    private Map<String, String> perm;

    /**
     * 匿名url列表
     */
    private List<String> anon;

    public Map<String, String> getPermDefine() {
        return permDefine;
    }

    public void setPermDefine(Map<String, String> permDefine) {
        this.permDefine = permDefine;
    }

    public Map<String, String> getPerm() {
        return perm;
    }

    public void setPerm(Map<String, String> perm) {
        this.perm = perm;
    }

    public List<String> getAnon() {
        return anon;
    }

    public void setAnon(List<String> anon) {
        this.anon = anon;
    }
}
