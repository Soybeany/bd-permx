package com.soybeany.demo.impl;

import com.soybeany.permx.core.perm.BasePermDefineProviderStdImpl;
import org.springframework.stereotype.Component;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
@Component
public class PermDefineProviderImpl extends BasePermDefineProviderStdImpl {
    @Override
    protected Class<?> onGetDefineClass() {
        return PermConstants.class;
    }
}
