package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookRepository;
import com.readingisgood.sale.domain.customer.Customer;
import com.readingisgood.sale.domain.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    BookRepository bookRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineRepository orderLineRepository;

    @Mock
    CustomerRepository customerRepository;

    @Test
    void it_should_create_order_when_stocks_are_available() {
        //given
        Long customerId = 123L;
        CreteOrder creteOrder = CreteOrder.builder()
                .customerId(customerId)
                .orderLine(CreteOrder.OrderLine.builder()
                        .bookId(23L)
                        .quantity(3)
                        .build())
                .orderLine(CreteOrder.OrderLine.builder()
                        .bookId(24L)
                        .quantity(1)
                        .build())
                .build();

        when(bookRepository.decreaseStock(23L, 3)).thenReturn(1);
        when(bookRepository.decreaseStock(24L, 1)).thenReturn(1);

        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(orderLineRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        Customer customer = new Customer();
        when(customerRepository.getReferenceById(customerId)).thenReturn(customer);
        Book book23 = new Book();
        Book book24 = new Book();
        when(bookRepository.getReferenceById(23L)).thenReturn(book23);
        when(bookRepository.getReferenceById(24L)).thenReturn(book24);

        //when
        OrderDetail orderDetail = orderService.order(creteOrder);


        //Then
        assertThat(orderDetail.getOrder().getCustomer()).isEqualTo(customer);
        assertThat(orderDetail.getOrderLines().size()).isEqualTo(2);
        assertThat(orderDetail.getOrderLines()).anySatisfy(ol -> {
            assertThat(ol.getOrder()).isEqualTo(orderDetail.getOrder());
            assertThat(ol.getBook()).isEqualTo(book23);
            assertThat(ol.getQuantity()).isEqualTo(3L);
        });
        assertThat(orderDetail.getOrderLines()).anySatisfy(ol -> {
            assertThat(ol.getOrder()).isEqualTo(orderDetail.getOrder());
            assertThat(ol.getBook()).isEqualTo(book24);
            assertThat(ol.getQuantity()).isEqualTo(1L);
        });
    }

    @Test
    void it_should_throw_OutOfStockException_when_any_unavailable_stock() {
        Long customerId = 123L;
        CreteOrder creteOrder = CreteOrder.builder()
                .customerId(customerId)
                .orderLine(CreteOrder.OrderLine.builder()
                        .bookId(23L)
                        .quantity(3)
                        .build())
                .orderLine(CreteOrder.OrderLine.builder()
                        .bookId(24L)
                        .quantity(1)
                        .build())
                .build();

        when(bookRepository.decreaseStock(23L, 3)).thenReturn(1);
        when(bookRepository.decreaseStock(24L, 1)).thenReturn(0);

        Throwable throwable = catchThrowable(() -> orderService.order(creteOrder));

        assertThat(throwable).isInstanceOf(OutOfStockException.class);
    }


    @Test
    void it_should_get_order_detail() {
        var orderId = 97L;
        Order order = new Order();
        when(orderRepository.findById(97L)).thenReturn(Optional.of(order));

        OrderLine orderLine = new OrderLine();
        when(orderLineRepository.findByOrder(order)).thenReturn(List.of(orderLine));

        //when
        Optional<OrderDetail> result = orderService.getOrder(orderId);

        //then
        assertThat(result).hasValueSatisfying(orderDetail -> {
                    assertThat(orderDetail.getOrder()).isEqualTo(order);
                    assertThat(orderDetail.getOrderLines()).containsExactly(orderLine);
                }
        );
    }

    @Test
    void it_should_get_order_detail_by_created_date() {
        //given
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(1);
        Order order = new Order();
        OrderLine orderLine = OrderLine.builder()
                .order(order).build();
        OrderLine orderLine2 = OrderLine.builder()
                .order(order).build();
        when(orderLineRepository.findByOrder_CreatedDateBetween(startDate, endDate)).thenReturn(List.of(orderLine,orderLine2));

        //when
        List<OrderDetail> orderDetails = orderService.getOrder(startDate, endDate);

        //then
        assertThat(orderDetails.size()).isEqualTo(1);
        assertThat(orderDetails.get(0).getOrder()).isEqualTo(order);
        assertThat(orderDetails.get(0).getOrderLines()).containsExactly(orderLine, orderLine2);
    }
}