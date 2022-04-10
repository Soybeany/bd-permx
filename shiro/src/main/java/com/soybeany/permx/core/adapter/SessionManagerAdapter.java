package com.soybeany.permx.core.adapter;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/4/2
 */
@Component
public class SessionManagerAdapter extends DefaultWebSessionManager implements BeanPostProcessor {

    public SessionManagerAdapter(SessionDAO sessionDAO, Cookie cookieTemplate) {
        setSessionDAO(sessionDAO);
        setSessionIdCookie(cookieTemplate);
        setSessionValidationSchedulerEnabled(false);
    }

    @Override
    protected void onStart(Session session, SessionContext context) {
        // 变量初始化
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        // 保存
        getSessionIdCookie().saveTo(request, response);
        // 其它
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }
}
