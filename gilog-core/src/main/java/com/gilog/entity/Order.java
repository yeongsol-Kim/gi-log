package com.gilog.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "product")
    private String product;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date_list")
    private String dateList;

    @Column(name = "waybill_number")
    private String waybillNumber;

    @Column(name = "pay_state")
    private Integer payState;

    @Column(name = "delivery_state")
    private Integer deliveryState;

    @Column(name = "address")
    private String address;

    @Column(name = "price")
    private Integer price;

    @Column(name = "refund_state")
    private Integer refundState;
}
