package com.soybeany.permx.impl;

import com.soybeany.exception.BdRtException;
import com.soybeany.permx.api.ISession;
import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.util.cache.IDataHolder;
import com.soybeany.util.cache.StdMemDataHolder;

import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class SessionStorageStdImpl<Session extends ISession> implements ISessionStorage<Session> {

    private final IDataHolder<Session> sessionHolder = onSetupSessionHolder();

    @Override
    public int getSessionTtl(String sessionId, Session session) {
        return 30 * 60;
    }

    @Override
    public void saveSession(String sessionId, Session session, int ttl, boolean canCreate) {
        if (!canCreate && null == sessionHolder.get(sessionId)) {
            throw new BdRtException("不允许保存不存在的会话");
        }
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
