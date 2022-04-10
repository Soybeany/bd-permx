package com.soybeany.permx.core.auth;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.IAuthListener;
import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.api.InputAccessor;
import com.soybeany.permx.core.adapter.AuthenticationTokenAdapter;
import com.soybeany.permx.core.adapter.SessionDaoAdapter;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapException;
import com.soybeany.permx.core.exception.ShiroAuthenticationWrapRtException;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
        } catch (ShiroAuthenticationWrapRtException e) {
            throw e.getTarget();
        } finally {
            inputInputAccessor.removeInput();
        }
        // 调用回调
        try {
            Session session = SessionDaoAdapter.loadSession();
            listener.onFoundSession(session);
        } catch (BdPermxNoSessionException e) {
            throw new BdRtException(e.getMessage());
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // shiro退出登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

}
