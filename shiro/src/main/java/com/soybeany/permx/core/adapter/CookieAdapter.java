package com.soybeany.permx.core.adapter;

import com.soybeany.permx.api.ISession;
import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.api.InputAccessor;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/4/9
 */
@Component
public class CookieAdapter<Input, Session extends ISession> extends SimpleCookie implements BeanPostProcessor {

    @Lazy
    @Autowired
    private InputAccessor<Input> inputAccessor;
    @Lazy
    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Lazy
    @Autowired
    private ISessionStorage<Session> sessionStorage;

    @Override
    public void saveTo(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = SessionDaoAdapter.getSessionId();
        Session session = SessionDaoAdapter.loadSession();
        Input input = inputAccessor.getInput();
        int ttl = sessionStorage.getSessionTtl(sessionId, session);
        sessionIdProcessor.saveSessionId(sessionId, request, response, input, ttl);
    }

    @Override
    public String readValue(HttpServletRequest request, HttpServletResponse ignored) {
        try {
            return sessionIdProcessor.loadSessionId(request);
        } catch (BdPermxNoSessionException e) {
            return null;
        }
    }

    @Override
    public void removeFrom(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = SessionDaoAdapter.getSessionId();
        sessionIdProcessor.removeSessionId(sessionId, request, response);
    }

}
