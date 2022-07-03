package com.gilog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class OrderFilter {

    private String searchKey;

    private String searchValue;

    private String product;

    private Integer payState;

    private LocalDate beforeDate;

    private LocalDate afterDate;
}
