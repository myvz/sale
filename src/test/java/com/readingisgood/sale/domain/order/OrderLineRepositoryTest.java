package com.readingisgood.sale.domain.order;

import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class OrderLineRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Test
    void findByOrder() {

        Customer customer = createCustomer();

        Book book = createBook("Robert Martin", "Clean Code", "Software Design", 1000L, BigDecimal.valueOf(136), "978 - 1 - 56619 - 909 - 4");
        Book book2 = createBook("Robert Martin", "The Clean Coder", "Software Design", 1000L, BigDecimal.valueOf(230), "978-1-56619-909-4");

        Order order = createOrder(customer);
        OrderLine orderLine = createOrderLine(book, order, 2);
        OrderLine orderLine2 = createOrderLine(book2, order, 3);

        testEntityManager.clear();

        List<OrderLine> result = orderLineRepository.findByOrder(order);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(orderLine, orderLine2);
    }


    @Test
    void findByOrder_CreatedDateBetween() {

        Customer customer = createCustomer();

        Book book = createBook("Robert Martin", "Clean Code", "Software Design", 1000L, BigDecimal.valueOf(136), "978 - 1 - 56619 - 909 - 4");
        Book book2 = createBook("Robert Martin", "The Clean Coder", "Software Design", 1000L, BigDecimal.valueOf(230), "978-1-56619-909-4");

        Order order = createOrder(customer);
        OrderLine orderLine = createOrderLine(book, order, 2);
        OrderLine orderLine2 = createOrderLine(book2, order, 3);

        testEntityManager.clear();

        List<OrderLine> result = orderLineRepository.findByOrder_CreatedDateBetween(LocalDateTime.now()
                .minusDays(1), LocalDateTime.now());

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(orderLine, orderLine2);
    }

    @Test
    void it_should_get_montlhy_customer_order_stats() {

        Customer customer = createCustomer();

        Book book = createBook("Robert Martin", "Clean Code", "Software Design", 1000L, BigDecimal.valueOf(136), "978 - 1 - 56619 - 909 - 4");
        Book book2 = createBook("Robert Martin", "The Clean Coder", "Software Design", 1000L, BigDecimal.valueOf(230), "978-1-56619-909-4");

        Order order = createOrder(customer);
        OrderLine orderLine = createOrderLine(book, order, 2);
        OrderLine orderLine2 = createOrderLine(book2, order, 3);

        Order order2 = createOrder(customer);
        OrderLine orderLine3 = createOrderLine(book, order2, 2);
        OrderLine orderLine4 = createOrderLine(book2, order2, 3);

        testEntityManager.clear();

        List<MonthlyCustomerOrderStatsView> result = orderLineRepository.findMonthlyCustomerOrderStatsByCustomerId(customer.getId());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getMonth()).isEqualTo(orderLine.getCreatedDate().getMonth().getValue());
        assertThat(result.get(0).getTotalOrderCount()).isEqualTo(2);
        assertThat(result.get(0).getTotalBookCount()).isEqualTo(10);
        assertThat(result.get(0).getTotalPurchasedAmount()).isEqualByComparingTo(BigDecimal.valueOf(1924));
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