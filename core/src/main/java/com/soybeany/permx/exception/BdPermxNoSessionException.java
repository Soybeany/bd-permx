package com.soybeany.permx.exception;

import com.soybeany.exception.BdException;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public class BdPermxNoSessionException extends BdException {
    public BdPermxNoSessionException(String message) {
        super(message);
    }
}
