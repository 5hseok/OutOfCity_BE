package com.outofcity.server.global.exception;


import com.outofcity.server.global.exception.message.ErrorMessage;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
