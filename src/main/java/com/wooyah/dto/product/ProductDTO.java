package com.wooyah.dto.product;

import com.wooyah.entity.Product;
import lombok.Builder;
import lombok.Data;

public class ProductDTO {
    @Data
    @Builder
    public static class Detail{
        private Long productId;
        private String productName;
        public static Detail from(Product product){
            return Detail.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .build();
        }
    }
}
