package com.gilog.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TodayOrderTotalDto {
    private Long count;
    private Long price;

    public TodayOrderTotalDto() {
        this.count = 0L;
        this.price = 0L;
    }
}
