package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.core.adapter.AuthenticationTokenAdapter;
import com.soybeany.permx.core.api.InputAccessor;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapException;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/28
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthManagerImpl<Input, Session> implements IAuthManager<Input> {

    @Autowired
    private ISessionManager<Input, Session> sessionManager;
    @Autowired
    private InputAccessor<Input> inputInputAccessor;
    @Autowired
    private IAuthListener<Session> listener;

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response, Input input, IAuthVerifier<Input> authVerifier) throws BdPermxAuthException {
        // shiro登录
        try {
            inputInputAccessor.setInput(input);
            SecurityUtils.getSubject().login(new AuthenticationTokenAdapter<>(input, authVerifier));
        } catch (ShiroAuthenticationWrapException e) {
            throw e.getTarget();
        } finally {
            inputInputAccessor.removeInput();
        }
        // 保存session
        Session session = sessionManager.saveSession(request, response, input);
        // 调用回调
        listener.onFoundSession(session);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {
        // shiro退出登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        // 移除session
        sessionManager.removeSession(request, response);
    }

}
