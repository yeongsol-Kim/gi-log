package com.gilog.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Long userId;

    private String nickname;

    private String product;

    private LocalDate orderDate;

    private Integer amount;

    private String dateList;

    private String waybillNumber;

    private Integer payState;

    private Integer deliveryState;

    private String address;

    private Integer price;

    private Integer refundState;

}
