package com.wooyah.dto.cartuser;

import com.wooyah.entity.enums.CartUserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartUserDTO {
    private CartUserStatus status;
}

