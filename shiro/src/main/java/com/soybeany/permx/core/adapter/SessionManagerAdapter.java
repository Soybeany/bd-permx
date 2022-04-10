package com.soybeany.permx.core.adapter;


import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class SessionManagerAdapter extends DefaultWebSessionManager {

    public SessionManagerAdapter(SessionDAO sessionDAO) {
        setSessionDAO(sessionDAO);
        setSessionValidationSchedulerEnabled(false);
    }

}
