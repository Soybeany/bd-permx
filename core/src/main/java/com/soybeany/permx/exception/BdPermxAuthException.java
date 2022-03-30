package com.soybeany.permx.exception;

import com.soybeany.exception.BdException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class BdPermxAuthException extends BdException {
    public BdPermxAuthException(String message) {
        super(message);
    }
}
