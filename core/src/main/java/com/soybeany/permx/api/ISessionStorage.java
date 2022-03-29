package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionStorage<Session> {

    int getSessionTtl();

    void saveSession(String sessionId, Session session);

    void removeSession(String sessionId) throws BdPermxNoSessionException;

    Session loadSession(String sessionId) throws BdPermxNoSessionException;

}
