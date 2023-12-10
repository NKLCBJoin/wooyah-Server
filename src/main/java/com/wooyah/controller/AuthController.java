package com.wooyah.controller;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.user.signup.UserSignUpDTO;
import com.wooyah.jwt.JwtAuthenticationProcessingFilter;
import com.wooyah.jwt.JwtService;
import com.wooyah.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public String jwtTest(HttpServletRequest request) {

        Long jwtExtractId = (Long) request.getAttribute("jwtExtractId");

        return "토큰 테스트 : 이메일에 대한 아이디는"+jwtExtractId;
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
