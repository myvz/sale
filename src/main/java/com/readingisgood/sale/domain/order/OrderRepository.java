package com.readingisgood.sale.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderLines", "orderLines.book"})
    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"orderLines", "orderLines.book"})
    List<Order> findByIdIn(List<Long> orders);

    Page<Order> findByCustomer_Id(long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"orderLines", "orderLines.book"})
    List<Order> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select month(ol.createdDate) as month," +
            " count(distinct o.id) as totalOrderCount ," +
            " sum(ol.quantity) as totalBookCount," +
            " sum(b.price * ol.quantity) as totalPurchasedAmount " +
            " from Order o join o.orderLines ol join ol.book b" +
            " where o.customer.id=:customerId group by month")
    List<MonthlyCustomerOrderStatsView> findMonthlyCustomerOrderStatsByCustomerId(Long customerId);

}
