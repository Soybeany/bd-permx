package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.InputAccessor;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/4/8
 */
@Component
public class InputAccessorImpl<Input> implements InputAccessor<Input> {

    private final ThreadLocal<Input> holder = new ThreadLocal<>();

    @Override
    public void setInput(Input input) {
        holder.set(input);
    }

    @Override
    public Input getInput() {
        return holder.get();
    }

    @Override
    public void removeInput() {
        holder.remove();
    }

}
