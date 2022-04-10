package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.CheckRule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionManager<Input, Session> {

    Session saveSession(HttpServletRequest request, HttpServletResponse response, Input input);

    void removeSession(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException;

    Session loadSession(HttpServletRequest request) throws BdPermxNoSessionException;

    boolean canAccess(CheckRule.WithPermission rule, Session session);

}
