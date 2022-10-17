package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class OrderRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void it_should_get_customer_orders() {

        Customer customer = createCustomer();

        Book book = createBook("Robert Martin", "Clean Code", "Software Design", 1000L, BigDecimal.valueOf(136), "978 - 1 - 56619 - 909 - 4");
        Book book2 = createBook("Robert Martin", "The Clean Coder", "Software Design", 1000L, BigDecimal.valueOf(230), "978-1-56619-909-4");

        Order order = createOrder(customer);
        OrderLine orderLine = createOrderLine(book, order, 2);
        OrderLine orderLine2 = createOrderLine(book2, order, 3);

        Order order2 = createOrder(customer);
        createOrderLine(book, order2, 2);
        createOrderLine(book2, order2, 3);

        Order order3 = createOrder(customer);
        createOrderLine(book, order3, 2);
        createOrderLine(book2, order3, 3);

        testEntityManager.clear();

        Page<Order> result = orderRepository.findByCustomer_Id(customer.getId(), PageRequest.of(0, 2));

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    private OrderLine createOrderLine(Book book, Order order, int quantity) {
        OrderLine orderLine = OrderLine.builder()
                .order(order)
                .book(book)
                .quantity(quantity)
                .build();
        return testEntityManager.persistAndFlush(orderLine);
    }

    private Order createOrder(Customer customer) {
        return testEntityManager.persistAndFlush(Order.builder()
                .customer(customer)
                .build());
    }

    private Book createBook(String author, String name, String genre, long stockCount, BigDecimal price, String isbn) {
        Book book = Book.builder().ISBN(isbn)
                .author(author)
                .name(name)
                .genre(genre)
                .stockCount(stockCount)
                .price(price)
                .build();
        return testEntityManager.persistAndFlush(book);
    }

    private Customer createCustomer() {
        String email = "johdoe@readingisgood.com";
        Customer customer = Customer.builder().email(email)
                .name("john")
                .lastName("doe")
                .build();
        return testEntityManager.persistAndFlush(customer);
    }

}