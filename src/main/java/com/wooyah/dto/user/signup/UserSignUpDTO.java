package com.wooyah.dto.user.signup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDTO {
    private String email;
    private String nickname;

    private String location;
    private String phone;
    private String device_number;
}
