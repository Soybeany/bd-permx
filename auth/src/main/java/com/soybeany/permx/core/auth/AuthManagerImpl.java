package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/28
 */
@Component
public class AuthManagerImpl<Input, Session> implements IAuthManager<Input> {

    @Autowired
    private ISessionManager<Input, Session> sessionManager;
    @Autowired
    private IAuthListener<Session> listener;

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response, Input input, IAuthVerifier<Input> authVerifier) throws BdPermxAuthException {
        // 认证
        authVerifier.onVerify(input);
        // 保存session
        Session session = sessionManager.saveSession(request, response, input);
        // 调用回调
        listener.onFoundSession(session);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {
        sessionManager.removeSession(request, response);
    }

}
