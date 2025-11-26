package com.qna.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseError {
    HttpStatus getStatus();
    String getMessage();
    String name();
}
