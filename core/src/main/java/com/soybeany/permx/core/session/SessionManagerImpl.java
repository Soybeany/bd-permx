package com.soybeany.permx.core.session;

import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.PermissionParts;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class SessionManagerImpl<Input, Session> implements ISessionManager<Input, Session> {

    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Autowired
    private ISessionProcessor<Input, Session> sessionProcessor;
    @Autowired
    private ISessionStorage<Input, Session> sessionStorage;

    @Override
    public Session saveSession(HttpServletRequest request, HttpServletResponse response, Input input) {
        // 获取sessionId
        String sessionId = sessionIdProcessor.getNewSessionId(input);
        // 入参转换为session实体
        Session session = sessionProcessor.toSession(sessionId, input);
        // 保存session实体
        int ttl = sessionStorage.getSessionTtl(input);
        sessionStorage.saveSession(sessionId, input, session, ttl);
        // 将sessionId写入response
        sessionIdProcessor.saveSessionId(sessionId, request, response, input, ttl);
        // 返回sessionId
        return session;
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

    @Override
    public Session loadSession(HttpServletRequest request) throws BdPermxNoSessionException {
        // 获取sessionId
        String sessionId = sessionIdProcessor.loadSessionId(request);
        // 获取session实体
        return sessionStorage.loadSession(sessionId);
    }

    @Override
    public Collection<PermissionParts> getPermissionsFromSession(Session session) {
        return sessionProcessor.getPermissionsFromSession(session);
    }

}
