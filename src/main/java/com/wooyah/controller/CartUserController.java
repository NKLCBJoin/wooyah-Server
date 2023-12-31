package com.wooyah.controller;


import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.cartuser.CartUserDTO;
import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.user.UserDTO;
import com.wooyah.entity.enums.CartUserStatus;
import com.wooyah.service.CartService;
import com.wooyah.service.CartUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/want")
public class CartUserController {

    private final CartUserService cartUserService;

    @PostMapping("/{cartId}")
    public ApiResponse<?> requestCartUser(@PathVariable("cartId") Long cartId, HttpServletRequest request)throws Exception{

        Long jwtExtractId = (Long) request.getAttribute("jwtExtractId");

        cartUserService.saveCartUser(cartId, jwtExtractId);

        return ApiResponse.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("장보기 요청이 등록 되었습니다.")
                .build();
    }

    @PostMapping("/{cartIdx}")
    public ApiResponse<CartUserDTO> requestCartResult(@PathVariable("cartIdx") Long cartId, HttpServletRequest request)throws Exception{

        Long jwtExtractId = (Long) request.getAttribute("jwtExtractId");

        CartUserDTO response = cartUserService.getCartUserStatus(cartId, jwtExtractId);

        return ApiResponse.<CartUserDTO>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("장보기 요청 결과값.")
                .result(response)
                .build();
    }


}
