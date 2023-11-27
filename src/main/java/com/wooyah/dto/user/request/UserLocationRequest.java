package com.wooyah.dto.user.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserLocationRequest {

    private BigDecimal latitude;
    private BigDecimal longitude;

}
