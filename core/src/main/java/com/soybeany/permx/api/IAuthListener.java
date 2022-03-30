package com.soybeany.permx.api;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface IAuthListener<Session> {

    /**
     * 请求开始时执行的回调
     */
    default void onStartRequest(HttpServletRequest request) {
    }

    /**
     * 请求结束时执行的回调
     */
    default void onFinishRequest(HttpServletRequest request) {
    }

    /**
     * 找到会话时的回调
     */
    default void onFoundSession(Session session) {
    }

}
