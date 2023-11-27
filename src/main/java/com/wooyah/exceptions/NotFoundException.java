package com.wooyah.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
