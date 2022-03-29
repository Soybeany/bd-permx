package com.soybeany.permx.api;

import com.soybeany.permx.exception.BdPermxAuthException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public interface IAuthVerifier<Input> {

    void onVerify(Input input) throws BdPermxAuthException;

}
