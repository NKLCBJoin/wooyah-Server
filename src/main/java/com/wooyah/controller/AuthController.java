package com.wooyah.controller;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.user.signup.UserSignUpDTO;
import com.wooyah.jwt.JwtService;
import com.wooyah.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;


    @GetMapping("/test")
    public String jwtTest(HttpServletResponse response) {

        //jwtService.sendAccessToken(response, jwtService.createAccessToken("123")); //임의로 이메일 추가하여 response에 토큰 달아서 보내기

        return "토큰 테스트";
    }

    @PostMapping("/api/auth/{sns}")
    public ApiResponse<?> exlogin(@RequestBody UserSignUpDTO userSignUpDto, HttpServletResponse response,
                               @PathVariable("sns") String sns)throws Exception {

        //서비스에 db 조회해서 있다면 로그인, 없다면 저장
        String msg = userService.emailcheck(userSignUpDto);

        //jwt 토큰 헤더에 추가
        jwtService.sendAccessToken(response, jwtService.createAccessToken(userSignUpDto.getEmail()));

        return ApiResponse.<CartDTO.Detail>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message(msg)
                .build();
    }
}