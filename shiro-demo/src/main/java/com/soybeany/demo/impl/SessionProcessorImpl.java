package com.soybeany.demo.impl;

import com.soybeany.demo.model.Input;
import com.soybeany.demo.model.Session;
import com.soybeany.permx.api.ISessionProcessor;
import com.soybeany.permx.model.PermissionParts;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@Component
public class SessionProcessorImpl implements ISessionProcessor<Input, Session> {

    private final Map<String, Collection<PermissionParts>> permissions = new HashMap<String, Collection<PermissionParts>>() {{
        put("456", Collections.singleton(PermissionParts.parse(PermConstants.API_COMMON)));
    }};

    @Override
    public Session toSession(String sessionId, Input input) {
        return new Session(Optional.ofNullable(permissions.get(input.getAccount()))
                .orElse(Collections.emptyList()));
    }

    @Override
    public Collection<PermissionParts> getPermissionsFromSession(Session session) {
        return session.getPermissions();
    }

}
