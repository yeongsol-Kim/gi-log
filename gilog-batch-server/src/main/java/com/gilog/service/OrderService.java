package com.gilog.service;

import com.gilog.dto.GiLogDto;
import com.gilog.dto.OrderDto;
import com.gilog.entity.GiLog;
import com.gilog.entity.Order;
import com.gilog.repository.GiLogRepository;
import com.gilog.repository.OrderRepository;
import com.gilog.repository.OrderRepositoryInt;
import com.gilog.repository.UserRepository;
import com.gilog.vo.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryInt orderRepositoryInt;
    private final GiLogRepository giLogRepository;


    public List<OrderDto> getAll() {
        List<Order> orders = orderRepositoryInt.findAll();
        List<OrderDto> ordersDto = new ArrayList<>();



        for(Order order : orders) {
            ordersDto.add(OrderDto.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .nickname(order.getNickname())
                    .product(order.getProduct())
                    .price(order.getPrice())
                    .deliveryState(order.getDeliveryState())
                    .orderDate(order.getOrderDate())
                    .waybillNumber(order.getWaybillNumber())
                    .payState(order.getPayState())
                    .build());
        }

        return ordersDto;
    }

    public List<OrderDto> getOrderList(OrderFilter orderFilter) {
        List<Order> orders = orderRepository.getOrderList(orderFilter);
        List<OrderDto> ordersDto = new ArrayList<>();


        for(Order order : orders) {
            ordersDto.add(OrderDto.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .nickname(order.getNickname())
                    .product(order.getProduct())
                    .address(order.getAddress())
                    .price(order.getPrice())
                    .deliveryState(order.getDeliveryState())
                    .orderDate(order.getOrderDate())
                    .waybillNumber(order.getWaybillNumber())
                    .payState(order.getPayState())
                    .refundState(order.getRefundState())
                    .build());
        }

        return ordersDto;
    }

    // 주문의 기록들 조회 (함수화 리팩터링)
    public List<GiLog> getOrderGiLogList(Long id) {
        Order order = orderRepositoryInt.findById(id).orElseThrow();
        List<LocalDate> dates = new ArrayList<>();
        //order.setDateList(order.getDateList().substring(1, order.getDateList().length() - 1));
        String[] stringDates = order.getDateList().split(",");

        for (String stringDate : stringDates) {
            dates.add(LocalDate.parse(stringDate.trim(), DateTimeFormatter.ISO_DATE));
        }

        List<GiLog> giLogs = giLogRepository.findByUserIdAndWriteDateIn(order.getUserId(), dates);
        return giLogs;
    }

    //결제 상태 변경
    public Long updateOrderPayState(Long id, Integer state) {
        Order order = orderRepositoryInt.findById(id).orElseThrow();
        order.setPayState(state);
        return orderRepositoryInt.save(order).getId();

    }

    //취소 상태 변경
    public Long updateOrderRefundState(Long id, Integer state) {
        Order order = orderRepositoryInt.findById(id).orElseThrow();
        order.setRefundState(state);
        return orderRepositoryInt.save(order).getId();

    }

    //배송 상태 변경
    public Long updateOrderDeliveryState(Long id, Integer state) {
        Order order = orderRepositoryInt.findById(id).orElseThrow();
        order.setDeliveryState(state);
        return orderRepositoryInt.save(order).getId();

    }

    //결제 상태 변경
    public Long updateOrderWayBillNumber(Long id, String wayBillNumber) {
        Order order = orderRepositoryInt.findById(id).orElseThrow();
        System.out.println(wayBillNumber);
        order.setWaybillNumber(wayBillNumber);
        return orderRepositoryInt.save(order).getId();

    }
}
