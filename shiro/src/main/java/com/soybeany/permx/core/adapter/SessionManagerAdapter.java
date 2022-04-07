package com.soybeany.permx.core.adapter;


import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.CheckRule;
import com.soybeany.permx.model.CheckRuleStorage;
import com.soybeany.permx.model.PermissionParts;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class SessionManagerAdapter<Input, S> extends DefaultSessionManager implements ISessionManager<Input, S> {

    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;
    @Autowired
    private ISessionStorage<S> sessionStorage;

    public SessionManagerAdapter() {
        setSessionValidationSchedulerEnabled(false);
    }

    @Override
    protected Session createExposedSession(Session session, SessionKey key) {
        return session;
    }

    @Override
    protected Session createExposedSession(Session session, SessionContext context) {
        return session;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S saveSession(HttpServletRequest request, HttpServletResponse response, Input input) {
        // 获取会话
        SessionAdapter<S> sessionAdapter = (SessionAdapter<S>) SecurityUtils.getSubject().getSession(false);
        // 将sessionId写入response
        String sessionId = (String) sessionAdapter.getId();
        int ttl = (int) (sessionAdapter.getTimeout() / 1000);
        sessionIdProcessor.saveSessionId(sessionId, request, response, input, ttl);
        return sessionAdapter.getSession();
    }

    @Override
    public void removeSession(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {
        // 获取sessionId
        String sessionId = sessionIdProcessor.loadSessionId(request);
        // 移除session实体
        sessionStorage.removeSession(sessionId);
        // 将移除sessionId的操作写入response
        sessionIdProcessor.removeSessionId(sessionId, request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    public S loadSession(HttpServletRequest request) throws BdPermxNoSessionException {
        String sessionId = sessionIdProcessor.loadSessionId(request);
        try {
            SessionAdapter<S> sessionAdapter = (SessionAdapter<S>) SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
            return sessionAdapter.getSession();
        } catch (UnknownSessionException e) {
            throw new BdPermxNoSessionException("没有找到会话");
        }
    }

    @Override
    public boolean canAccess(CheckRule.WithPermission rule, S session) {
        Collection<PermissionParts> provided = sessionProcessor.getPermissionsFromSession(session);
        return CheckRuleStorage.canAccess(rule, provided);
    }

}
