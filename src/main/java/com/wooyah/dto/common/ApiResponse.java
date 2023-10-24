package com.wooyah.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean isSuccess;
    private Integer code;
    private String message;
    private T result;
}
