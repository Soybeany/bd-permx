package com.soybeany.permx.api;

import com.soybeany.permx.model.PermissionDefine;

import java.util.Set;

/**
 * @author Soybeany
 * @date 2021/5/26
 */
public interface PermDefineProvider {

    Set<PermissionDefine> onGetPermDefines();

}
