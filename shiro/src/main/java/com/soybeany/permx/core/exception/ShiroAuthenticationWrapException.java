package com.soybeany.permx.core.exception;

import com.soybeany.permx.exception.BdPermxAuthException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public class ShiroAuthenticationWrapException extends AuthenticationException {

    private final BdPermxAuthException target;

    public ShiroAuthenticationWrapException(BdPermxAuthException target) {
        this.target = target;
    }

    public BdPermxAuthException getTarget() {
        return target;
    }

}
