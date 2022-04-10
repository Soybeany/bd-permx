package com.soybeany.permx.api;

/**
 * @author Soybeany
 * @date 2022/4/7
 */
public interface InputAccessor<Input> {

    void setInput(Input input);

    Input getInput();

    void removeInput();

}
