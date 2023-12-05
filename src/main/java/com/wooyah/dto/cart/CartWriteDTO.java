package com.wooyah.dto.cart;


import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.enums.CartStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartWriteDTO {
    private String location;
    private int participant_number;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> products;

}
