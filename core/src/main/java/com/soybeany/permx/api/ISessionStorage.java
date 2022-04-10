package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionStorage<Session> {

    int getSessionTtl(String sessionId, Session session);

    void saveSession(String sessionId, Object session, int ttl);

    void removeSession(String sessionId) throws BdPermxNoSessionException;

    <S> S loadSession(String sessionId) throws BdPermxNoSessionException;

}
