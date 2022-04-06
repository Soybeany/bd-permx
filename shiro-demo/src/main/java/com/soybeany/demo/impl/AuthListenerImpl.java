package com.soybeany.demo.impl;

import com.soybeany.demo.model.Session;
import com.soybeany.permx.api.IAuthListener;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/3/30
 */
@Component
public class AuthListenerImpl implements IAuthListener<Session> {

    @Override
    public void onFoundSession(Session session) {
        System.out.println("找到会话:" + session);
    }

}
