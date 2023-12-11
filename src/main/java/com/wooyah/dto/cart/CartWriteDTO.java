package com.wooyah.dto.cart;


import com.wooyah.dto.product.ProductDTO;
import com.wooyah.entity.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartWriteDTO {

    private String location;
    private int participantNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> products;

}
