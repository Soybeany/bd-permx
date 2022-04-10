package com.soybeany.permx.core.adapter;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.api.InputAccessor;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@SuppressWarnings("AlibabaServiceOrDaoClassShouldEndWithImpl")
@Component
public class SessionDaoAdapter<Input, S> implements SessionDAO, DisposableBean {

    private static final String REAL_SESSION = "realSession";
    private static final ThreadLocal<Session> STORAGE = new ThreadLocal<>();

    @Autowired
    private ISessionStorage<S> sessionStorage;
    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;
    @Autowired
    private InputAccessor<Input> inputAccessor;

    @SuppressWarnings("unchecked")
    public static <S> Optional<S> loadSessionOptional() {
        return Optional.ofNullable(SecurityUtils.getSubject().getSession(false))
                .map(session -> (S) session.getAttribute(REAL_SESSION));
    }

    @SuppressWarnings("unchecked")
    public static <S> S loadSession() throws BdPermxNoSessionException {
        return (S) loadSessionOptional().orElseThrow(() -> new BdPermxNoSessionException("没有找到会话"));
    }

    @Override
    public Serializable create(Session session) {
        Input input = inputAccessor.getInput();
        String sessionId = sessionIdProcessor.getNewSessionId(input);
        // 生成自定义session
        S s = sessionProcessor.toSession(sessionId, input);
        session.setAttribute(REAL_SESSION, s);
        // 持久化
        int ttl = sessionStorage.getSessionTtl(sessionId, s);
        sessionStorage.saveSession(sessionId, session, ttl);
        // 初始化
        initSession(session, sessionId, ttl);
        return sessionId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        // 若线程缓存中有，则直接使用
        Session session = STORAGE.get();
        if (null != session) {
            return session;
        }
        // 读取SimpleSession以及其中的自定义session，并保存到线程缓存
        try {
            String id = (String) sessionId;
            session = sessionStorage.loadSession(id);
            int ttl = sessionStorage.getSessionTtl(id, (S) session.getAttribute(REAL_SESSION));
            // 初始化
            initSession(session, id, ttl);
        } catch (BdPermxNoSessionException e) {
            throw new UnknownSessionException("没有找到指定的会话");
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        STORAGE.set(session);
    }

    @Override
    public void delete(Session session) {
        try {
            sessionStorage.removeSession((String) session.getId());
        } catch (BdPermxNoSessionException ignore) {
        }
        STORAGE.remove();
    }

    @Override
    public Collection<Session> getActiveSessions() {
        throw new BdRtException("不支持获取全部session的操作");
    }

    @Override
    public void destroy() {
        STORAGE.remove();
    }

    // ***********************内部方法****************************

    private void initSession(Session session, String sessionId, int ttl) {
        ((SimpleSession) session).setId(sessionId);
        session.setTimeout(ttl * 1000L);
        STORAGE.set(session);
    }

}
