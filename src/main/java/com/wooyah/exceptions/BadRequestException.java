package com.wooyah.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
