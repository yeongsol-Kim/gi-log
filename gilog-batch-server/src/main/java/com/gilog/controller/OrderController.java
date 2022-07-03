package com.gilog.controller;

import com.gilog.dto.OrderDto;
import com.gilog.service.OrderService;
import com.gilog.vo.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public String orderListPage() {

        return "order/orderList";
    }

    @GetMapping("/delivery")
    public String deliveryManagePage() {

        return "delivery/deliveryList";
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<OrderDto> orderListData(@RequestParam(required = false) String searchKey, @RequestParam(required = false) String searchValue, @RequestParam(required = false) String product, @RequestParam(required = false) String beforeDateString, @RequestParam(required = false) String afterDateString, @RequestParam(required = false) Integer payState) {

        //널값 변환
        product = (product.equals("on")) ? null : product;

        // 날짜 처리
        if (beforeDateString.isEmpty()) { beforeDateString = "2099-01-01"; }
        if (afterDateString.isEmpty()) { afterDateString = "2000-01-01"; }

        LocalDate beforeDate = LocalDate.parse(beforeDateString, DateTimeFormatter.ISO_DATE);
        LocalDate afterDate = LocalDate.parse(afterDateString, DateTimeFormatter.ISO_DATE);

        OrderFilter filter =  OrderFilter.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .payState(payState)
                .product(product)
                .beforeDate(beforeDate)
                .afterDate(afterDate)
                .build();

        return orderService.getOrderList(filter);
    }




//    @GetMapping("/ordersss")
//    @ResponseBody
//    public List<OrderDto> orderListPagesss(@RequestParam(required = false) String product, @RequestParam(required = false) LocalDate toDate) {
//
//        return a;
//    }

}
