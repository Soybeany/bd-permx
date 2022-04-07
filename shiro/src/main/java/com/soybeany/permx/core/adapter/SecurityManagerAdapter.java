package com.soybeany.permx.core.adapter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class SecurityManagerAdapter extends DefaultSecurityManager implements InitializingBean {

    @Autowired
    private Realm realm;
    @Autowired
    private SubjectFactory subjectFactory;
    @Autowired
    private DefaultSessionManager sessionManager;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public void afterPropertiesSet() {
        setRealm(realm);
        setSubjectFactory(subjectFactory);
        // 修改session创建
        sessionManager.setSessionFactory(sessionFactory);
        // 修改session存储
        sessionManager.setSessionDAO(sessionDAO);
        setSessionManager(sessionManager);
        // 全局注册
        SecurityUtils.setSecurityManager(this);
    }

}
