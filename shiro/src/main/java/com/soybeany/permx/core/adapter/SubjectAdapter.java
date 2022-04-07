package com.soybeany.permx.core.adapter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DelegatingSubject;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public class SubjectAdapter extends DelegatingSubject {

    public SubjectAdapter(PrincipalCollection principals, boolean authenticated, String host, Session session, boolean sessionCreationEnabled, SecurityManager securityManager) {
        super(principals, authenticated, host, session, sessionCreationEnabled, securityManager);
    }

    @Override
    protected Session decorate(Session session) {
        return session;
    }

}
