package com.soybeany.permx.core.adapter;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
public class SecurityManagerAdapter extends DefaultWebSecurityManager {

    public SecurityManagerAdapter(Realm singleRealm) {
        super(singleRealm);
        DefaultWebSessionManager sessionManager = new SessionManagerAdapter();
        // 修改session创建
//        sessionManager.setSessionFactory(context -> {
//
//        });
        // 修改session存储
//        sessionManager.setSessionDAO();
        // 修改cookie
//        sessionManager.setSessionIdCookie();
        setSessionManager(sessionManager);
    }

}
