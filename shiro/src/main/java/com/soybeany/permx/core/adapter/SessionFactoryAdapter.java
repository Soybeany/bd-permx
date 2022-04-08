package com.soybeany.permx.core.adapter;

import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.core.api.InputAccessor;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@Component
public class SessionFactoryAdapter<Input, S> implements SessionFactory {

    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;
    @Autowired
    private ISessionStorage<S> sessionStorage;
    @Autowired
    private InputAccessor<Input> inputAccessor;

    public static <S> Session createShiroSession(String sessionId, S s, ISessionStorage<S> sessionStorage) {
        SessionAdapter<S> session = new SessionAdapter<>(s);
        // 配置shiro会话
        session.setId(sessionId);
        session.setTimeout(sessionStorage.getSessionTtl(sessionId, s) * 1000L);
        return session;
    }

    @Override
    public Session createSession(SessionContext initData) {
        Input input = inputAccessor.getInput();
        String sessionId = sessionIdProcessor.getNewSessionId(input);
        // 创建自定义会话
        S s = sessionProcessor.toSession(sessionId, input);
        // 转换为shiro会话
        return createShiroSession(sessionId, s, sessionStorage);
    }

}
