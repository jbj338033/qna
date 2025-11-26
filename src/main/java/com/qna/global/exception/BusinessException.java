package com.qna.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BaseError error;

    public BusinessException(BaseError error) {
        super(error.getMessage());
        this.error = error;
    }
}
