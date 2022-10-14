package com.readingisgood.sale.api.domain.order;

import com.readingisgood.sale.domain.order.OrderDetail;
import com.readingisgood.sale.domain.order.OrderService;
import com.readingisgood.sale.domain.util.DateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailResponse createOrder(@RequestBody @Validated OrderRequest orderRequest) {
        OrderDetail order = orderService.order(orderMapper.mapToCreateOrder(orderRequest));
        return orderMapper.mapToOrderResponse(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId)
                .map(orderMapper::mapToOrderResponse)
                .map(ResponseEntity.ok()::body)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<OrderDetailResponse> getOrder(@RequestParam("startDate") Long startDateEpochTime,
                                              @RequestParam("endDate") Long endDateEpochTime) {
        return orderService.getOrder(DateUtil.fromEpochSeconds(startDateEpochTime), DateUtil.fromEpochSeconds(endDateEpochTime))
                .stream()
                .map(orderMapper::mapToOrderResponse)
                .collect(Collectors.toList());
    }
}
