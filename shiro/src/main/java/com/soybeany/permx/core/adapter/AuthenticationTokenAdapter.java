package com.soybeany.permx.core.adapter;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public class AuthenticationTokenAdapter<Input> extends UsernamePasswordToken {

    private static final String VALUE = "OK";

    private final Input input;

    public AuthenticationTokenAdapter(Input input) {
        super(VALUE, VALUE);
        this.input = input;
    }

    public Input getInput() {
        return input;
    }

}
