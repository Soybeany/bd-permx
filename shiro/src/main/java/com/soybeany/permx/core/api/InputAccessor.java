package com.soybeany.permx.core.api;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public interface InputAccessor<T> {

    void setInput(T input);

    T getInput();

    void removeInput();

}
