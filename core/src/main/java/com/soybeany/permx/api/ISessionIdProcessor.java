package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionIdProcessor<Input> {

    String getNewSessionId(Input input);

    String loadSessionId(HttpServletRequest request) throws BdPermxNoSessionException;

    void saveSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response, Input input, int ttl);

    void removeSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response);

}
