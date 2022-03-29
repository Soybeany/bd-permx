package com.soybeany.demo.impl;

import com.soybeany.permx.annotation.PermDefine;

/**
 * @author Soybeany
 * @date 2021/12/23
 */
public interface PermConstants {

    @PermDefine(name = "接口-通用", desc = "允许访问通用接口")
    String API_COMMON = "api:common";

}
