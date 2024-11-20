package com.outofcity.server.global.exception;


import com.outofcity.server.global.exception.message.ErrorMessage;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
