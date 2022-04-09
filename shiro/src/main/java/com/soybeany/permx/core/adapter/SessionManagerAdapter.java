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
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class SessionManagerAdapter<Input, S> extends DefaultWebSessionManager implements ISessionManager<Input, S> {

    private static final String REAL_SESSION = "realSession";

    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;
    @Autowired
    private ISessionStorage<S> sessionStorage;

    @SuppressWarnings("unchecked")
    public static <S> Optional<S> loadSession() {
        return Optional.ofNullable(SecurityUtils.getSubject().getSession(false))
                .map(session -> (S) session.getAttribute(REAL_SESSION));
    }

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

    @Override
    public S saveSession(HttpServletRequest request, HttpServletResponse response, Input input) {
        // 获取会话
        Session session = SecurityUtils.getSubject().getSession(false);
        // 将自定义session写入session
        String sessionId = (String) session.getId();
        S s = sessionProcessor.toSession(sessionId, input);
        session.setAttribute(REAL_SESSION, s);
        return s;
    }

    @Override
    public void removeSession(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {
        // 获取sessionId
        String sessionId = sessionIdProcessor.loadSessionId(request);

    }

    @SuppressWarnings("unchecked")
    @Override
    public S loadSession(HttpServletRequest request) throws BdPermxNoSessionException {
        return (S) loadSession().orElseThrow(() -> new BdPermxNoSessionException("没有找到会话"));
    }

    @Override
    public boolean canAccess(CheckRule.WithPermission rule, S session) {
        Collection<PermissionParts> provided = sessionProcessor.getPermissionsFromSession(session);
        return CheckRuleStorage.canAccess(rule, provided);
    }

}
