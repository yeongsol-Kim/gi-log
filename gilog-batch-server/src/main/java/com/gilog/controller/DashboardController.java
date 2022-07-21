package com.gilog.controller;

import com.gilog.dto.OrderDto;
import com.gilog.entity.Order;
import com.gilog.service.OrderService;
import com.gilog.vo.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final OrderService orderService;

    @GetMapping("/")
    public String dashboard(Model model) {
        OrderFilter dailyOrderFilter = OrderFilter.builder().afterDate(LocalDate.now()).build();
        List<OrderDto> todayOrders = orderService.getOrderList(dailyOrderFilter);

        int todayOrderCount = 0;
        int todayOrderPrice = 0;
        for (OrderDto order : todayOrders) {
            todayOrderCount++;
            todayOrderPrice += order.getPrice();
        }


        OrderFilter monthOrderFilter = OrderFilter.builder().afterDate(LocalDate.now().minusMonths(1)).build();
        List<OrderDto> monthOrders = orderService.getOrderList(monthOrderFilter);

        int monthPay1Count = 0;
        int monthDelivery1Count = 0;
        int monthDelivery3Count = 0;
        int monthDelivery4Count = 0;
        for (OrderDto order : monthOrders) {
            if (order.getPayState() == 1) monthPay1Count++;
            if (order.getDeliveryState() == 1) monthDelivery1Count++;
            if (order.getDeliveryState() == 3) monthDelivery3Count++;
            if (order.getDeliveryState() == 4) monthDelivery4Count++;
        }

        model.addAttribute("todayOrderCount", todayOrderCount);
        model.addAttribute("todayOrderPrice", todayOrderPrice);

        model.addAttribute("monthPay1Count", monthPay1Count);
        model.addAttribute("monthDelivery1Count", monthDelivery1Count);
        model.addAttribute("monthDelivery3Count", monthDelivery3Count);
        model.addAttribute("monthDelivery4Count", monthDelivery4Count);

        return "dashboard";
    }

}
