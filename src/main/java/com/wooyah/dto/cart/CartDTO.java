package com.wooyah.dto.cart;

import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartProduct;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CartDTO {
    @Data
    @Builder
    public static class Detail{
        private Long cartId;
        private String nickname;
        private String shoppingLocation;
        private List<ProductDTO.Detail> products;

        public static Detail from(Cart cart){
            List<ProductDTO.Detail> productDetails = cart.getCartProducts().stream()
                    .map(CartProduct::getProduct)
                    .map(ProductDTO.Detail::from)
                    .toList();

            return Detail.builder()
                    .cartId(cart.getId())
                    .nickname(cart.getOwnerNickname())
                    .shoppingLocation(cart.getShoppingLocation())
                    .products(productDetails)
                    .build();
        }
    }

}
