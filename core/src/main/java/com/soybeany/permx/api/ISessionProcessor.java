package com.soybeany.permx.api;

import com.soybeany.permx.model.PermissionParts;

import java.util.Collection;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface ISessionProcessor<Input, Session> {

    Session toSession(String sessionId, Input input);

    Collection<PermissionParts> getPermissionsFromSession(Session session);

}
