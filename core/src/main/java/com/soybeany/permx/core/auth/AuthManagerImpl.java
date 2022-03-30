package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/28
 */
public class AuthManagerImpl<Input, Session> implements IAuthManager<Input> {

    @Autowired
    private ISessionManager<Input, Session> sessionManager;

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, Input input, IAuthVerifier<Input> authVerifier) throws BdPermxAuthException {
        // 认证
        authVerifier.onVerify(input);
        // 保存session，并返回sessionId
        return sessionManager.saveSession(request, response, input);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {
        sessionManager.removeSession(request, response);
    }

}
