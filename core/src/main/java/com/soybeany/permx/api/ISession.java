package com.soybeany.permx.api;

/**
 * @author Soybeany
 * @date 2022/4/10
 */
public interface ISession {

    void setAttribute(String key, String value);

    String getAttribute(String key);

}
