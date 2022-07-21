package com.gilog.service;

import com.gilog.dto.OrderDto;
import com.gilog.entity.Order;
import com.gilog.entity.User;
import com.gilog.repository.OrderRepository;
import com.gilog.repository.OrderRepositoryInt;
import com.gilog.repository.UserRepositoryInt;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepositoryInt orderRepositoryInt;
    private final UserRepositoryInt userRepositoryInt;

    public List<OrderDto> getMyOrderList(String username) {
        User user = userRepositoryInt.findByUsername(username).orElse(null);
        List<OrderDto> ordersDto = new ArrayList<>();
        if (user == null) {
            return null;
        }
        List<Order> orders = orderRepositoryInt.findByUserId(user.getId());
        for (Order order: orders) {
            ordersDto.add(
                    OrderDto.builder()
                            .id(order.getId())
                            .orderDate(order.getOrderDate())
                            .price(order.getPrice())
                            .deliveryState(order.getDeliveryState())
                            .payState(order.getPayState())
                            .build()
            );
        }
        return ordersDto;
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepositoryInt.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .waybillNumber(order.getWaybillNumber())
                .nickname(order.getNickname())
                .price(order.getPrice())
                .orderDate(order.getOrderDate())
                .userId(order.getUserId())
                .product(order.getProduct())
                .payState(order.getPayState())
                .deliveryState(order.getDeliveryState())
                .amount(order.getAmount())
                .dateList(order.getDateList())
                .address(order.getAddress())
                .build();
    }

    public Long addOrder(OrderDto orderDto) {


        Order order = Order.builder()
                .id(orderDto.getId())
                .userId(orderDto.getUserId())
                .product(orderDto.getProduct())
                .orderDate(orderDto.getOrderDate())
                .amount(orderDto.getAmount())
                .dateList(orderDto.getDateList())
                .address(orderDto.getAddress())
                .price(orderDto.getPrice())
                .payState(1)
                .deliveryState(1)
                .refundState(0)
                .nickname(userRepositoryInt.findById(orderDto.getUserId()).orElse(null).getNickname())
                .build();

        return orderRepositoryInt.save(order).getId();
    }
}
