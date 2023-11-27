package com.wooyah.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    USER_NOT_FOUND("해당하는 사용자를 찾을 수 없습니다"),
    CART_NOT_FOUND("해당하는 카트를 찾을 수 없습니다"),
    CART_USER_NOT_FOUND("해당 카트에 유저가 참가 하고 있지 않습니다."),
    USER_DOES_NOT_OWN_CART("해당 카트의 주인이 아닙니다."),
    CART_OWNER_NOT_FOUND("카트 주인을 찾을 수 없습니다.");
    private final String message;
}
