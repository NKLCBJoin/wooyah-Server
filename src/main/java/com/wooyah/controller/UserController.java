package com.wooyah.controller;

import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.dto.user.UserDTO;
import com.wooyah.dto.user.request.UserLocationRequest;
import com.wooyah.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/phone")
    public ApiResponse<UserDTO.Phone> getPhoneNumber( HttpServletRequest request ){
        Long userId = (Long) request.getAttribute("jwtExtractId");
        UserDTO.Phone response = userService.getPhoneNum(userId);
        return ApiResponse.<UserDTO.Phone>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("마이페이지 전화번호")
                .result(response)
                .build();
    }

    @PutMapping("location")
    public ApiResponse<?> enterUserLocation(@RequestBody UserLocationRequest userLocation,
                                            HttpServletRequest request){
        Long userId = (Long) request.getAttribute("jwtExtractId");
        userService.updateUserLocation(userLocation, userId);
        return ApiResponse.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("좌표가 정상적으로 등록되었습니다.")
                .build();
    }

    @GetMapping("/mypage")
    public ApiResponse<PaginationListDTO<UserDTO.Detail>> getMyPageCarts(Pageable page,
                                                                         HttpServletRequest request){
        Long userId = (Long) request.getAttribute("jwtExtractId");
        PaginationListDTO<UserDTO.Detail> myCarts = userService.getMyCarts(userId);

        return ApiResponse.<PaginationListDTO<UserDTO.Detail>>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("마이페이지 작성 글 목록")
                .result(myCarts)
                .build();
    }
}
