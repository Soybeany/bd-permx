package com.soybeany.permx.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public class ShiroAuthenticationWrapRtException extends AuthenticationException {

    private final RuntimeException target;

    public ShiroAuthenticationWrapRtException(RuntimeException target) {
        this.target = target;
    }

    public RuntimeException getTarget() {
        return target;
    }

}
