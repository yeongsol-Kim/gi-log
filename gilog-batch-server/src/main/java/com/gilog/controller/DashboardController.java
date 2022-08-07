package com.gilog.controller;

import com.gilog.dto.MonthOrderTotalDto;
import com.gilog.dto.OrderDto;
import com.gilog.dto.TodayOrderTotalDto;
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

        model.addAttribute("todayTotal", todayTotal());
        model.addAttribute("monthTotal", monthTotal());

        return "dashboard";
    }

    // 일일 통계
    private TodayOrderTotalDto todayTotal() {

        OrderFilter dailyOrderFilter = OrderFilter.builder().afterDate(LocalDate.now()).build();
        List<OrderDto> todayOrders = orderService.getOrderList(dailyOrderFilter);

        TodayOrderTotalDto todayTotal = new TodayOrderTotalDto();
        for (OrderDto order : todayOrders) {
            todayTotal.setCount(todayTotal().getCount() + 1);
            todayTotal.setPrice(todayTotal().getPrice() + order.getPrice());
        }

        return todayTotal;
    }

    //최근 1개월 통계
    private MonthOrderTotalDto monthTotal() {
        OrderFilter monthOrderFilter = OrderFilter.builder().afterDate(LocalDate.now().minusMonths(1)).build();
        List<OrderDto> monthOrders = orderService.getOrderList(monthOrderFilter);

        MonthOrderTotalDto monthTotal = new MonthOrderTotalDto();
        for (OrderDto order : monthOrders) {
            if (order.getPayState() == 1) monthTotal.setPay1(monthTotal.getPay1() + 1);

            if (order.getDeliveryState() == 1) monthTotal.setDelivery1(monthTotal.getDelivery1() + 1);
            else if (order.getDeliveryState() == 3) monthTotal.setDelivery3(monthTotal.getDelivery3() + 1);
            else if (order.getDeliveryState() == 4) monthTotal.setDelivery4(monthTotal.getDelivery4() + 1);

            if (order.getRefundState() == 1) monthTotal.setRefund1(monthTotal.getRefund1() + 1); // 취소/교환/반품/환불
            else if (order.getRefundState() == 2) monthTotal.setRefund2(monthTotal.getRefund2() + 1);
            else if (order.getRefundState() == 3) monthTotal.setRefund3(monthTotal.getRefund3() + 1);
            else if (order.getRefundState() == 4) monthTotal.setRefund4(monthTotal.getRefund4() + 1);
        }

        return monthTotal;
    }
}
