package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionStorage<Input, Session> {

    int getSessionTtl(Input input);

    void saveSession(String sessionId, Input input, Session session, int ttl);

    void removeSession(String sessionId) throws BdPermxNoSessionException;

    Session loadSession(String sessionId) throws BdPermxNoSessionException;

}
