package com.soybeany.permx.core.adapter;

import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@Component
public class SubjectFactoryAdapter extends DefaultSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        SecurityManager securityManager = context.resolveSecurityManager();
        Session session = context.resolveSession();
        boolean sessionCreationEnabled = context.isSessionCreationEnabled();
        PrincipalCollection principals = context.resolvePrincipals();
        boolean authenticated = context.resolveAuthenticated();
        String host = context.resolveHost();
        return new SubjectAdapter(principals, authenticated, host, session, sessionCreationEnabled, securityManager);
    }

}
