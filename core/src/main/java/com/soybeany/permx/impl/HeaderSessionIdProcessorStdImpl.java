package com.soybeany.permx.impl;

import com.soybeany.permx.exception.BdPermxNoSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class HeaderSessionIdProcessorStdImpl<Input> extends BaseSessionIdProcessorStdImpl<Input> {

    @Override
    public String loadSessionId(HttpServletRequest request) throws BdPermxNoSessionException {
        return Optional
                .ofNullable(request.getHeader(sessionIdKey))
                .orElseThrow(() -> new BdPermxNoSessionException("header中没有提供sessionId"));
    }

    @Override
    public void saveSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(sessionIdKey, sessionId);
    }

    @Override
    public void removeSessionId(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        // 不需作处理
    }

}
