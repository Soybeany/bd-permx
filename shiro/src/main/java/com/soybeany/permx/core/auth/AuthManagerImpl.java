package com.soybeany.permx.core.auth;

import com.soybeany.permx.api.IAuthManager;
import com.soybeany.permx.api.IAuthVerifier;
import com.soybeany.permx.exception.BdPermxAuthException;
import com.soybeany.permx.exception.BdPermxNoSessionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Soybeany
 * @date 2022/3/28
 */
public class AuthManagerImpl<Input, Session> implements IAuthManager<Input> {

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response, Input input, IAuthVerifier<Input> authVerifier) throws BdPermxAuthException {

    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws BdPermxNoSessionException {

    }

}
