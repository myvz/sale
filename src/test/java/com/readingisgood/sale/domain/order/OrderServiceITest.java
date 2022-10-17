package com.readingisgood.sale.domain.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderServiceITest {

    @Autowired
    OrderService orderService;

    @Test
    void it_should_create_order() {

    }

}