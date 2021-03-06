package com.soybeany.permx.core.adapter;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.*;
import com.soybeany.permx.core.config.PermxShiroConfig;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.util.HexUtils;
import com.soybeany.util.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@SuppressWarnings("AlibabaServiceOrDaoClassShouldEndWithImpl")
@Component
public class SessionDaoAdapter<Input, S extends ISession> implements BeanPostProcessor, SessionDAO, ServletRequestListener {

    private static final String REAL_SESSION = "ShiroSession";
    private static final ThreadLocal<Object> STORAGE = new ThreadLocal<>();
    private static final ThreadLocal<Session> SHIRO_STORAGE = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> CAN_CREATE_SESSION = new ThreadLocal<>();
    private static final Map<Serializable, Lock> LOCK_MAP = new ConcurrentHashMap<>();

    @Lazy
    @Autowired
    private ISessionStorage<S> sessionStorage;
    @Lazy
    @Autowired
    private ISessionIdProcessor<Input> sessionIdProcessor;
    @Lazy
    @Autowired
    private ISessionProcessor<Input, S> sessionProcessor;
    @Lazy
    @Autowired
    private InputAccessor<Input> inputAccessor;
    @Lazy
    @Autowired
    private PermxShiroConfig permxShiroConfig;

    @SuppressWarnings("unchecked")
    public static <S> Optional<S> loadSessionOptional() {
        return Optional.ofNullable((S) STORAGE.get());
    }

    @SuppressWarnings("unchecked")
    public static <S> S loadSession() {
        return (S) loadSessionOptional().orElseThrow(() -> new BdRtException("??????????????????"));
    }

    public static String getSessionId() {
        return (String) SHIRO_STORAGE.get().getId();
    }

    @Override
    public Serializable create(Session session) {
        Input input = inputAccessor.getInput();
        String id = sessionIdProcessor.getNewSessionId(input);
        // ???????????????session
        S s = sessionProcessor.toSession(id, input);
        // ?????????shiro session
        int ttl = sessionStorage.getSessionTtl(id, s);
        ((SimpleSession) session).setId(id);
        session.setTimeout(ttl * 1000L);
        // ?????????
        updateSessionStorage(s, session, true);
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        String id = (String) sessionId;
        //???????????????session
        S s = (S) STORAGE.get();
        if (null == s) {
            try {
                s = sessionStorage.loadSession(id);
                STORAGE.set(s);
            } catch (BdPermxNoSessionException e) {
                throw new UnknownSessionException("???????????????????????????");
            }
        }
        // ???????????????????????????????????????
        Session session = SHIRO_STORAGE.get();
        if (null != session) {
            return session;
        }
        // ???????????????session??????Session???????????????????????????
        try {
            session = SerializeUtils.deserialize(HexUtils.hexToByteArray(s.getAttribute(REAL_SESSION)));
            SHIRO_STORAGE.set(session);
        } catch (IOException | ClassNotFoundException e) {
            throw new BdRtException("??????????????????session:" + e.getMessage());
        }
        return session;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (!permxShiroConfig.getEnableUpdateSession() && !Boolean.TRUE.equals(CAN_CREATE_SESSION.get())) {
            return;
        }
        S s = (S) loadSessionOptional().orElseThrow(() -> new UnknownSessionException("??????????????????"));
        updateSessionStorage(s, session, false);
    }

    @Override
    public void delete(Session session) {
        CAN_CREATE_SESSION.set(null);
        Serializable sessionId = session.getId();
        runWithLock(sessionId, false, () -> {
            try {
                sessionStorage.removeSession((String) sessionId);
            } catch (BdPermxNoSessionException ignore) {
            }
        });
    }

    @Override
    public Collection<Session> getActiveSessions() {
        throw new BdRtException("?????????????????????session?????????");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // ???????????????
        Boolean canCreate = CAN_CREATE_SESSION.get();
        if (null != canCreate) {
            Session session = SHIRO_STORAGE.get();
            runWithLock(session.getId(), true, () -> {
                S s = (S) STORAGE.get();
                sessionStorage.saveSession((String) session.getId(), s, (int) (session.getTimeout() / 1000), canCreate);
            });
        }
        STORAGE.remove();
        SHIRO_STORAGE.remove();
        CAN_CREATE_SESSION.remove();
    }

    // ***********************????????????****************************

    @SuppressWarnings("AlibabaLockShouldWithTryFinally")
    private void runWithLock(Serializable sessionId, boolean useTry, Runnable runnable) {
        Lock lock;
        synchronized (LOCK_MAP) {
            lock = LOCK_MAP.computeIfAbsent(sessionId, id -> new ReentrantLock());
        }
        if (useTry) {
            if (!lock.tryLock()) {
                return;
            }
        } else {
            lock.lock();
        }
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    private void updateSessionStorage(S s, Session session, boolean canCreateSession) {
        String sessionString;
        try {
            sessionString = HexUtils.bytesToHex(SerializeUtils.serialize(session));
        } catch (IOException e) {
            throw new BdRtException("???????????????session:" + e.getMessage());
        }
        s.setAttribute(REAL_SESSION, sessionString);
        STORAGE.set(s);
        SHIRO_STORAGE.set(session);
        if (null == CAN_CREATE_SESSION.get() || canCreateSession) {
            CAN_CREATE_SESSION.set(canCreateSession);
        }
    }

}
