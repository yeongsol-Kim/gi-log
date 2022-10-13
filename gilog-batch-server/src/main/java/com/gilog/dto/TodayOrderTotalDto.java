package com.gilog.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TodayOrderTotalDto {
    private int count;
    private Long price;

    public TodayOrderTotalDto() {
        this.count = 0;
        this.price = 0L;
    }
}
