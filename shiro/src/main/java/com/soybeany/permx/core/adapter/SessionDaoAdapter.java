package com.soybeany.permx.core.adapter;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@SuppressWarnings({"AlibabaServiceOrDaoClassShouldEndWithImpl", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class SessionDaoAdapter<S> implements SessionDAO {

    @Autowired
    private ISessionStorage<S> sessionStorage;

    @Override
    public Serializable create(Session session) {
        return session.getId();
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try {
            String id = (String) sessionId;
            S s = sessionStorage.loadSession(id);
            return SessionFactoryAdapter.createShiroSession(id, s, sessionStorage);
        } catch (BdPermxNoSessionException e) {
            throw new UnknownSessionException("没有找到指定的会话");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Session session) throws UnknownSessionException {
        SessionAdapter<S> sessionAdapter = (SessionAdapter<S>) session;
        String sessionId = (String) sessionAdapter.getId();
        S s = sessionAdapter.getSession();
        int ttl = (int) (session.getTimeout() / 1000);
        sessionStorage.saveSession(sessionId, s, ttl);
    }

    @Override
    public void delete(Session session) {
        try {
            sessionStorage.removeSession((String) session.getId());
        } catch (BdPermxNoSessionException ignore) {
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        throw new BdRtException("不支持获取全部session的操作");
    }

}
