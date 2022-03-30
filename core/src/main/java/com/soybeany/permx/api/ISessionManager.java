package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxNoSessionException;
import com.soybeany.permx.model.PermissionParts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionManager<Input, Session> {

    Session saveSession(HttpServletRequest request, HttpServletResponse response, Input input);

    void removeSession(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException;

    Session loadSession(HttpServletRequest request) throws BdPermxNoSessionException;

    Collection<PermissionParts> getPermissionsFromSession(Session session);

}
