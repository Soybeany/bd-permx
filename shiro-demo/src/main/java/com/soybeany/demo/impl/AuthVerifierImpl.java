package com.soybeany.demo.impl;

import com.soybeany.demo.model.Input;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.exception.BdPermxAuthException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@Component
public class AuthVerifierImpl implements IAuthVerifier<Input> {

    private final Map<String, String> users = new HashMap<String, String>() {{
        put("123", "abc");
        put("456", "abc");
    }};

    @Override
    public void onVerify(Input input) throws BdPermxAuthException {
        Optional.ofNullable(users.get(input.getAccount()))
                .filter(pwd -> pwd.equals(input.getPwd()))
                .orElseThrow(() -> new BdPermxAuthException("账号或密码错误"));
    }

}
