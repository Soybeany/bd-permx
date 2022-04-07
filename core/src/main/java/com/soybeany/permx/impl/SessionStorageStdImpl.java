package com.soybeany.permx.impl;

import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.util.cache.IDataHolder;
import com.soybeany.util.cache.StdMemDataHolder;

import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class SessionStorageStdImpl<Session> implements ISessionStorage<Session> {

    private final IDataHolder<Session> sessionHolder = onSetupSessionHolder();

    @Override
    public int getSessionTtl(String sessionId, Session session) {
        return 30 * 60;
    }

    @Override
    public void saveSession(String sessionId, Session session, int ttl) {
        sessionHolder.put(sessionId, session, ttl);
    }

    @Override
    public void removeSession(String sessionId) throws BdPermxNoSessionException {
        Optional.ofNullable(sessionHolder.remove(sessionId))
                .orElseThrow(() -> new BdPermxNoSessionException("session不存在"));
    }

    @Override
    public Session loadSession(String sessionId) throws BdPermxNoSessionException {
        return Optional.ofNullable(sessionHolder.get(sessionId))
                .orElseThrow(() -> new BdPermxNoSessionException("session不存在"));
    }

    protected IDataHolder<Session> onSetupSessionHolder() {
        return new StdMemDataHolder<>(onSetupMaxSessionCount());
    }

    protected int onSetupMaxSessionCount() {
        return 100;
    }

}
