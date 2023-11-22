package com.wooyah.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String email;
    private String nickname;
    private String phone;
}
