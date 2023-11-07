package com.wooyah.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginationListDTO<T> {

    private Number count;
    private List<T> data;

}