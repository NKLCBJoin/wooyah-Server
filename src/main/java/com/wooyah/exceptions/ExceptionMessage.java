package com.wooyah.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    USER_NOT_FOUND("해당하는 사용자를 찾을 수 없습니다"),
    CART_NOT_FOUND("해당하는 카트를 찾을 수 없습니다");

    private final String message;
}
