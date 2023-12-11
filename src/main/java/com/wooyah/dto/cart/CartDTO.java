package com.wooyah.dto.cart;

import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.Cart;
import com.wooyah.entity.CartProduct;
import com.wooyah.entity.enums.CartStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
    @Data
    @Builder
    public static class Detail{
        private Long cartId;
        private String ownerPhoneNumber;
        private String nickname;
        private String shoppingLocation;
        private List<ProductDTO.Detail> products;
        private CartStatus cartStatus;

        public static Detail from(Cart cart){
            List<ProductDTO.Detail> productDetails = cart.getCartProducts().stream()
                    .map(CartProduct::getProduct)
                    .map(ProductDTO.Detail::from)
                    .toList();

            return Detail.builder()
                    .cartId(cart.getId())
                    .ownerPhoneNumber(cart.getOwnerPhoneNumber())
                    .cartStatus(cart.getStatus())
                    .nickname(cart.getOwnerNickname())
                    .shoppingLocation(cart.getShoppingLocation())
                    .products(productDetails)
                    .build();
        }
    }

    @Data
    @Builder
    public static class Near {
        private Long cartId;
        private BigDecimal latitude;
        private BigDecimal longitude;

        public static Near from(Cart cart) {
            return Near.builder()
                    .cartId(cart.getId())
                    .latitude(cart.getLatitude())
                    .longitude(cart.getLongitude())
                    .build();
        }
    }

}
