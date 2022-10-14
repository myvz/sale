package com.readingisgood.sale.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findByOrder(Order order);

    List<OrderLine> findByOrder_CreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
