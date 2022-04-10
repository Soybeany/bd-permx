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

    private final IDataHolder<Object> sessionHolder = onSetupSessionHolder();

    @Override
    public int getSessionTtl(String sessionId, Session session) {
        return 30 * 60;
    }

    @Override
    public void saveSession(String sessionId, Object session, int ttl) {
        sessionHolder.put(sessionId, session, ttl);
    }

    @Override
    public void removeSession(String sessionId) throws BdPermxNoSessionException {
        Optional.ofNullable(sessionHolder.remove(sessionId))
                .orElseThrow(() -> new BdPermxNoSessionException("session不存在"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> S loadSession(String sessionId) throws BdPermxNoSessionException {
        return Optional.ofNullable((S) sessionHolder.get(sessionId))
                .orElseThrow(() -> new BdPermxNoSessionException("session不存在"));
    }

    protected IDataHolder<Object> onSetupSessionHolder() {
        return new StdMemDataHolder<>(onSetupMaxSessionCount());
    }

    protected int onSetupMaxSessionCount() {
        return 100;
    }

}
