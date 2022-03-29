package com.soybeany.permx.impl;

import com.soybeany.permx.core.perm.PermDefineProvider;
import com.soybeany.permx.model.PermissionDefine;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class EmptyPermDefineProvider implements PermDefineProvider {
    @Override
    public Set<PermissionDefine> onGetPermDefines() {
        return null;
    }
}
