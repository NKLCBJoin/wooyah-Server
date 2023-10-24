package com.wooyah.controller;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.product.ProductDTO;
import com.wooyah.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @GetMapping("/{cartId}")
    public ApiResponse<CartDTO.Detail> getCartDetail(@PathVariable Long cartId){
        CartDTO.Detail response =cartService.getDetail(cartId);
        return ApiResponse.<CartDTO.Detail>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("글 세부 정보")
                .result(response)
                .build();
    }
}
