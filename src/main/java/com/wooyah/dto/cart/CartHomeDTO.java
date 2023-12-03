package com.wooyah.dto.cart;

import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartProduct;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CartHomeDTO {
    @Data
    @Builder
    public static class Detail {
        private int count;
        private List<CartDTO.Detail> data;

        public static Detail from (List<Cart> carts){
            List<CartDTO.Detail> homeCartsDetails = carts.stream()
                    .map(CartDTO.Detail::from)
                    .toList();

            return Detail.builder()
                    .count(1)
                    .data(homeCartsDetails)
                    .build();
        }
    }

}
