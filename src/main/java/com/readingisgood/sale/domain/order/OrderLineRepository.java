package com.readingisgood.sale.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findByOrder(Order order);

    List<OrderLine> findByOrder_CreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select month(ol.createdDate) as month," +
            " count(distinct o.id) as totalOrderCount ," +
            " sum(ol.quantity) as totalBookCount," +
            " sum(b.price * ol.quantity) as totalPurchasedAmount " +
            " from OrderLine ol join ol.order o join ol.book b" +
            " where o.customer.id=:customerId group by month")
    List<MonthlyCustomerOrderStatsView> findMonthlyCustomerOrderStatsByCustomerId(Long customerId);
}
