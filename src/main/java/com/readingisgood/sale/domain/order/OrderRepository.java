package com.readingisgood.sale.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderLines","orderLines.book"})
    Page<Order> findByCustomer_Id(long customerId, Pageable pageable);
}
