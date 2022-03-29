package com.soybeany.permx.api;

import com.soybeany.permx.model.CheckRule;

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
     * 鉴权时的回调
     */
    default void onCheckAuth(HttpServletRequest request, CheckRule rule, Session session, boolean permitted) {
    }

}
