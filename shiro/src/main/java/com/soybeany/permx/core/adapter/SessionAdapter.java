package com.soybeany.permx.core.adapter;

import org.apache.shiro.session.mgt.SimpleSession;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
public class SessionAdapter<Session> extends SimpleSession {

    private final Session session;

    public SessionAdapter(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

}
