package com.soybeany.permx.impl;

import com.soybeany.permx.api.ISession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Soybeany
 * @date 2022/4/10
 */
public class SessionImpl implements ISession {

    private final Map<String, String> storage = new HashMap<>();

    @Override
    public void setAttribute(String key, String value) {
        storage.put(key, value);
    }

    @Override
    public String getAttribute(String key) {
        return storage.get(key);
    }

}
