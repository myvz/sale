package com.readingisgood.sale.api.domain.order;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private Long customerId;

    @Singular
    @NotEmpty
    private List<OrderLineRequest> orderLineRequests;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class OrderLineRequest {
        @NotNull
        private Long bookId;

        @NotNull
        private Integer quantity;
    }
}
