package com.soybeany.permx.impl;

import com.soybeany.permx.api.ISessionStorage;
import com.soybeany.permx.exception.BdPermxNoSessionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class CookieSessionIdProcessorStdImpl<Input> extends BaseSessionIdProcessorStdImpl<Input> {

    @Autowired
    private ISessionStorage<?> sessionStorage;

    @Override
    public String loadSessionId(HttpServletRequest request) throws BdPermxNoSessionException {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            throw new BdPermxNoSessionException("没有可用的cookies");
        }
        for (Cookie cookie : cookies) {
            if (sessionIdKey.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new BdPermxNoSessionException("cookies中没有提供sessionId");
    }

    @Override
    public void saveSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        setupCookie(request, response, sessionIdKey, sessionId, onSetupCookieMaxAge());
    }

    @Override
    public void removeSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        setupCookie(request, response, sessionIdKey, sessionId, 0);
    }

    /**
     * 配置cookie的会话有效期
     */
    protected int onSetupCookieMaxAge() {
        return sessionStorage.getSessionTtl();
    }

    // ***********************内部方法****************************

    private void setupCookie(HttpServletRequest request, HttpServletResponse response, String sessionIdKey, String sessionId, Integer maxAge) {
        Cookie cookie = new Cookie(sessionIdKey, sessionId);
        cookie.setPath(request.getContextPath());
        if (null != maxAge) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

}
