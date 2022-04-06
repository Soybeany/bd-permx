package com.soybeany.permx.core.adapter;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.ResourceBasedWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class EnvironmentLoaderListenerAdapter extends EnvironmentLoaderListener {

    @Override
    protected Class<? extends WebEnvironment> getDefaultWebEnvironmentClass() {
        return EnvImpl.class;
    }

    public static class EnvImpl extends ResourceBasedWebEnvironment {
    }

}
