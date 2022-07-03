package com.gilog.service;

import com.gilog.dto.OrderDto;
import com.gilog.entity.Order;
import com.gilog.repository.OrderRepository;
import com.gilog.vo.OrderFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
                    .orderDate(order.getOrderDate())
                    .waybillNumber(order.getWaybillNumber())
                    .payState(order.getPayState())
                    .build());
        }

        return ordersDto;
    }
}
