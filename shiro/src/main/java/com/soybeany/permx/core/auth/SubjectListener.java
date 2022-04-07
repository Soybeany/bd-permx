package com.soybeany.permx.core.auth;

import org.apache.shiro.util.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
@Component
class SubjectListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ThreadContext.unbindSubject();
    }

}
