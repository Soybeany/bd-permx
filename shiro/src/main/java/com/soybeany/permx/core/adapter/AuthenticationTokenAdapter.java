package com.soybeany.permx.core.adapter;

import com.soybeany.permx.api.IAuthVerifier;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public class AuthenticationTokenAdapter<Input> extends UsernamePasswordToken {

    private static final String VALUE = "OK";

    private final Input input;
    private final IAuthVerifier<Input> authVerifier;

    public AuthenticationTokenAdapter(Input input, IAuthVerifier<Input> authVerifier) {
        super(VALUE, VALUE);
        this.input = input;
        this.authVerifier = authVerifier;
    }

    public Input getInput() {
        return input;
    }

    public IAuthVerifier<Input> getAuthVerifier() {
        return authVerifier;
    }

}
