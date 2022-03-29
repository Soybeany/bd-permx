package com.soybeany.permx.core.session;

import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionManager;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private ISessionStorage<Session> sessionStorage;

    @Override
    public String saveSession(HttpServletRequest request, HttpServletResponse response, Input input) {
        // 获取sessionId
        String sessionId = sessionIdProcessor.getNewSessionId(input);
        // 入参转换为session实体
        Session session = sessionProcessor.toSession(sessionId, input);
        // 保存session实体
        sessionStorage.saveSession(sessionId, session);
        // 将sessionId写入response
        sessionIdProcessor.saveSessionId(sessionId, request, response);
        // 返回sessionId
        return sessionId;
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

}
