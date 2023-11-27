package com.wooyah.controller;

import com.wooyah.dto.cart.CartDTO;
import com.wooyah.dto.common.ApiResponse;
import com.wooyah.dto.common.PaginationListDTO;
import com.wooyah.dto.product.ProductDTO;
import com.wooyah.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @GetMapping("/{cartId}")
    public ApiResponse<CartDTO.Detail> getCartDetail(@PathVariable Long cartId){
        CartDTO.Detail response = cartService.getDetail(cartId);

        return ApiResponse.<CartDTO.Detail>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("글 세부 정보")
                .result(response)
                .build();
    }

    @GetMapping("/locations")
    public ApiResponse<PaginationListDTO<CartDTO.Near>> getNearCarts(@RequestParam(value="zoom", defaultValue="1") int zoom){
        // TODO 해당 부분 JWT 이용해서 변경
        Long userId = 1L;
        PaginationListDTO<CartDTO.Near> nearCarts = cartService.getNearCarts(userId, zoom);

        return ApiResponse.<PaginationListDTO<CartDTO.Near>>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("지도 카트 목록 표시")
                .result(nearCarts)
                .build();
    }


    @GetMapping("/near")
    public ApiResponse<PaginationListDTO<CartDTO.Near>> getNearCartsByLatAndLong(@RequestParam BigDecimal latitude,
                                                                                 @RequestParam BigDecimal longitude,
                                                                                 @RequestParam(value="zoom", defaultValue="1") int zoom){
        PaginationListDTO<CartDTO.Near> nearCarts = cartService.getNearCarts(latitude, longitude, zoom);

        return ApiResponse.<PaginationListDTO<CartDTO.Near>>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("요청받은 위/경도 기준 가까운 카트 목록 표시")
                .result(nearCarts)
                .build();
    }

    @DeleteMapping
    public ApiResponse<?> deleteCart(@RequestParam(value="cartId") Long cartId){
        Long userId = 1L;
        cartService.deleteCart(userId, cartId);

        return ApiResponse.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("작성글이 정상적으로 삭제되었습니다.")
                .build();
    }
}
