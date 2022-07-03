package com.gilog.controller;

import com.gilog.dto.OrderDto;
import com.gilog.service.OrderService;
import com.gilog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/api/order/{id}")
    public OrderDto getMyOrder(@PathVariable Long id) {

        return orderService.getOrderById(id);
    }

    @GetMapping("/api/order")
    public List<OrderDto> getMyOrder(Authentication authentication) {
        return orderService.getMyOrderList(authentication.getName());
    }

    @PostMapping("/api/order")
    public Long addOrder(@RequestBody OrderDto orderDto, Authentication authentication) {
        orderDto.setUserId(userService.getIdByUsername(authentication.getName()));

        return orderService.addOrder(orderDto);
    }
}
